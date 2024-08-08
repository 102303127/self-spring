package com.zhang.web.servlet;

import com.zhang.web.servlet.handler.HandlerExecutionChain;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhang
 * @date 2024/7/28
 * @Description
 */
public interface HandlerMapping extends Ordered {

    String BEST_MATCHING_HANDLER_ATTRIBUTE = HandlerMapping.class.getName() + ".bestMatchingHandler";


    @Deprecated
    String LOOKUP_PATH = HandlerMapping.class.getName() + ".lookupPath";


    String PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE = HandlerMapping.class.getName() + ".pathWithinHandlerMapping";


    String BEST_MATCHING_PATTERN_ATTRIBUTE = HandlerMapping.class.getName() + ".bestMatchingPattern";


    String INTROSPECT_TYPE_LEVEL_MAPPING = HandlerMapping.class.getName() + ".introspectTypeLevelMapping";


    String URI_TEMPLATE_VARIABLES_ATTRIBUTE = HandlerMapping.class.getName() + ".uriTemplateVariables";


    String MATRIX_VARIABLES_ATTRIBUTE = HandlerMapping.class.getName() + ".matrixVariables";


    String PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE = HandlerMapping.class.getName() + ".producibleMediaTypes";

    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
