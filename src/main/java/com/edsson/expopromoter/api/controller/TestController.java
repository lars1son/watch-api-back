package com.edsson.expopromoter.api.controller;

import com.edsson.expopromoter.api.model.json.GenericResponse;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.request.TestRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/test")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Api(value = "Admin", description = "Administrator controller")
public class TestController {
    private final ImageOperator imageOperator;

    @Autowired
    public TestController(ImageOperator imageOperator) {
        this.imageOperator = imageOperator;
    }

    @CrossOrigin
    @RequestMapping(
            value = "/aws_image",
            method = POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE}
    )
    public GenericResponse addtoaws(@RequestBody TestRequest testRequest,
                                    BindingResult bindingResult,
                                    HttpServletResponse response,
                                    HttpServletRequest request) throws Exception {
        imageOperator.saveImage("testFile.jpg",testRequest.getImage());
//        return new GenericResponse("Uploaded",new String[] {});
        return new GenericResponse( new String[]{});
    }
}
