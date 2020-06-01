package com.crossecore.generator.openapi;

import io.swagger.model.Project;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.UUID;

import com.crossecore.CrossEcore;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.api.GenerateApi;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreValidator;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
@Controller
public class MyController implements GenerateApi{

    private static final Logger log = LoggerFactory.getLogger(MyController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public MyController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }
    

    public ResponseEntity<String> generateProjectFilenameGet(@ApiParam(value = "uuid of the project.",required=true) @PathVariable("project") UUID project,@ApiParam(value = "name of an generated file.",required=true) @PathVariable("filename") String filename) {
        
    	//TODO get file from database
    	EPackage epackage = EcorePackage.eINSTANCE;
    	
    	//TODO check if filename is valid
    	String filecontents = CrossEcore.generate(epackage, filename);
    	String accept = request.getHeader("Accept");
        return ResponseEntity.ok()
				.body(filecontents);

    }

    public ResponseEntity<Project> index(@ApiParam(value = "file",required=true) @PathVariable("project") UUID project,@ApiParam(value = "file",required=true) @PathVariable("language") String language,@ApiParam(value = "Contents of the model file as XMI."  )  @Valid @RequestBody Object body) {
        
    	
    	EPackage epackage = null;
    	if(body!=null && body instanceof String) {
    		String contents = body.toString();
    		//TODO store in database
    		
    	}
    	else {
    		XMIResourceImpl b = new XMIResourceImpl();
    		InputStream inputStream = new ByteArrayInputStream(body.toString().getBytes());
    		try {
				b.doLoad(inputStream, Collections.EMPTY_MAP);
				EObject root = b.getContents().get(0);
				
				epackage = (EPackage) root;
				
				//EcoreValidator.validate(root, new DiagnosticChain(), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//TODO get file from database	
    	}
    	
    	String accept = request.getHeader("Accept");
        return new ResponseEntity<Project>(HttpStatus.NOT_IMPLEMENTED);
    }

}
