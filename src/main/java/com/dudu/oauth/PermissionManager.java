package com.dudu.oauth;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.*;

public class PermissionManager {
    private final Logger logger = LoggerFactory.getLogger(PermissionManager.class);

    // Path Node is prefix by method
    private static final String URL_TREE_ROOT_VALUE = "dudu";
    private PathNode urlPublicTree;
    private Map<String, PathNode> urlTreesByScope;

    // endpoint + method
    private Set<String> urlsWithMethods;

    public PermissionManager(String file) throws Exception {
        try (var in = new FileInputStream(file)) {
            var config = new JSONObject(new JSONTokener(in));

            // loading endpoints
            urlsWithMethods = new HashSet<>();
            var apiEndpointMap = new HashMap<Long, ApiEndpoint>();
            var apiEndpointsJson = config.getJSONArray("ApiEndpoints");
            for (int i = 0; i < apiEndpointsJson.length(); i++) {
                var apiEndpointJson = apiEndpointsJson.getJSONObject(i);
                var apiEndpoint = new ApiEndpoint();
                apiEndpoint.setApiEndpointId(apiEndpointJson.getLong("ApiEndpointId"));
                apiEndpoint.setMethod(apiEndpointJson.getString("Method"));
                apiEndpoint.setEndpoint(apiEndpointJson.getString("Endpoint"));
                apiEndpoint.setPublic(apiEndpointJson.has("IsPublic") && apiEndpointJson.getBoolean("IsPublic"));

                apiEndpointMap.put(apiEndpoint.getApiEndpointId(), apiEndpoint);
                urlsWithMethods.add(apiEndpoint.getEndpoint() + apiEndpoint.getMethod());
            }

            // scopes
            var scopeList = new ArrayList<Scope>();
            var scopes = config.getJSONArray("Scopes");
            for (int i = 0; i < scopes.length(); i++) {
                var scopeJson = scopes.getJSONObject(i);
                var scope = new Scope();
                scope.setScopeName(scopeJson.getString("ScopeName"));

                if (scopeJson.has("ApiEndpointIds")) {
                    var apiEndpointIdsJson = scopeJson.getJSONArray("ApiEndpointIds");

                    scope.setEndpoints(new HashSet<>());
                    for (int j = 0; j < apiEndpointIdsJson.length(); j++) {
                        var apiEndpointId = apiEndpointIdsJson.getLong(j);
                        scope.getEndpoints().add(apiEndpointMap.get(apiEndpointId));
                    }
                }

                scopeList.add(scope);
            }

            urlTreesByScope = new HashMap<>();
            for (var scope : scopeList) {
                var apiEndpointSet = scope.getEndpoints();
                var apiEndpointArray = new ApiEndpoint[apiEndpointSet.size()];
                apiEndpointSet.toArray(apiEndpointArray);
                urlTreesByScope.put(scope.getScopeName(), buildTree(apiEndpointArray));
            }

            urlPublicTree = new PathNode(URL_TREE_ROOT_VALUE);
            var apiEndpointList = new ArrayList<ApiEndpoint>();
            for (var apiEndpoint : apiEndpointMap.values()) {

                if (apiEndpoint.isPublic())
                    apiEndpointList.add(apiEndpoint);

            }
            var apiEndpointArray = new ApiEndpoint[apiEndpointList.size()];
            urlPublicTree = buildTree(apiEndpointList.toArray(apiEndpointArray));

            logger.info("permission configured successfully");
        }
    }




    PathNode buildTree(ApiEndpoint[] apiEndpoints) {
        PathNode urlTree = new PathNode(URL_TREE_ROOT_VALUE);

        for (var apiEndpoint : apiEndpoints) {
            List<String> nodeValueList = breakIntoNodes(apiEndpoint);

            // head node is already added
            // FIXME, can we do better...
            nodeValueList.remove(0);

            var curNode = urlTree;
            for (var nodeValue : nodeValueList) {

                if (curNode.getChildren() == null)
                    curNode.setChildren(new HashMap<>());

                PathNode nextNode = curNode.getChildren().get(nodeValue);
                if (nextNode == null) {
                    nextNode = new PathNode(nodeValue);
                    curNode.getChildren().put(nextNode.getValue(), nextNode);
                }

                curNode = nextNode;
            }

            // endpoint node
            curNode.setApiEndpoint(apiEndpoint);
        }

        return urlTree;
    }

    /**
     *
     * @param scope
     * @param endpoint
     * @param method
     * @return strict mode, not found will return false
     */
    public boolean isPermitted(String scope, String endpoint, String method) {
        var nodeValueList =  breakIntoNodes(endpoint, method);

        // check public tree
        if (isPublic(endpoint, method))
            return true;

        // check scope trees
        var urlTree = urlTreesByScope.get(scope);
        if (urlTree == null)
            return false;

        var apiEndpoint = match(urlTree, nodeValueList, 0);
        return apiEndpoint != null;
    }

    public boolean isPublic(String endpoint, String method) {
        var nodeValueList =  breakIntoNodes(endpoint, method);

        // check public tree
        return match(urlPublicTree, nodeValueList, 0) != null;
    }

    public boolean contains(String endpoint, String method) {
        return urlsWithMethods.contains(endpoint+method);
    }

    /**
     *
     * @param node
     * @param nodeValueList
     * @param pos
     * @return null on not found
     */
    ApiEndpoint match(PathNode node, List<String> nodeValueList, int pos) {
        String nodeValue = nodeValueList.get(pos);
        if (!nodeValue.equals(node.getValue()) && !node.getValue().equals("*"))
            return null;

        if (nodeValueList.size() - 1 == pos)
            return node.getApiEndpoint(); // found it

        if (node.getChildren() == null)
            return null; // not more child nodes to search

        // continue searching
        for (PathNode nextNode : node.getChildren().values()) {
            var apiEndpoint = match(nextNode, nodeValueList, pos+1);
            if (apiEndpoint != null)
                return apiEndpoint;
        }

        // search exhausted.
        return null;
    }

    private List<String> breakIntoNodes(ApiEndpoint apiEndpoint) {
        return breakIntoNodes(apiEndpoint.getEndpoint(), apiEndpoint.getMethod());
    }

    private List<String> breakIntoNodes(String endpoint, String method) {
        var nodeValueList = new ArrayList<String>();
        nodeValueList.add(URL_TREE_ROOT_VALUE);
        nodeValueList.add(method);
        for (String nodeValue : endpoint.split("/"))
            if (!nodeValue.equals(""))
                nodeValueList.add(nodeValue);

        return nodeValueList;
    }
}
