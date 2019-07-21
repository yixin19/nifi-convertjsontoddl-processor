/**
 * 
 */
package com.dataflowdeveloper.processors.convertjsontoddl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.FLOAD;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.MockPropertyValue;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Before;
import org.junit.Test;

import com.dataflowdeveloper.processors.convertjsontoddl.JsonToDDLProcessor;


/**
 * @author tspann
 *
 */
public class JsonToDDLProcessorTest {

	private TestRunner testRunner;

	@Before
	public void init() {
		testRunner = TestRunners.newTestRunner(JsonToDDLProcessor.class);
	}

	@Test
	public void processor_should_produce_DDL(){

		try {
			final String filename = "simple.json";
			MockFlowFile flowFile = testRunner.enqueue(new FileInputStream(new File("src/test/resources/" + filename)));
			Map<String, String> attrs = new HashMap<String, String>() {
				{
					put("filename", filename);			
				}
			};
			flowFile.putAttributes(attrs);
			testRunner.setValidateExpressionUsage(false);
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_NAME, "simple");
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_TYPE, "hive");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		testRunner.assertValid();
		testRunner.run();
		testRunner.assertTransferCount(JsonToDDLProcessor.REL_FAILURE, 0);

		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(JsonToDDLProcessor.REL_SUCCESS);
		for (MockFlowFile mockFile : successFiles) {
			try {

				testRunner.assertAllFlowFilesTransferred(JsonToDDLProcessor.REL_SUCCESS);
//				for (String attribute : mockFile.getAttributes().keySet()) {
//					System.out.println("Attribute:" + attribute + "=" + mockFile.getAttribute(attribute));
//				}

				mockFile.assertAttributeExists(JsonToDDLProcessor.FIELD_DDL);
				assertTrue(  mockFile.getAttribute(JsonToDDLProcessor.FIELD_DDL).contains("CREATE TABLE") );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void processor_should_work_for_large_fields() {

		try {
			final String filename = "complex.json";
			MockFlowFile flowFile = testRunner.enqueue(new FileInputStream(new File("src/test/resources/" + filename)));
			Map<String, String> attrs = new HashMap<String, String>() {
				{
					put("filename", filename);			
				}
			};
			flowFile.putAttributes(attrs);
			testRunner.setValidateExpressionUsage(false);
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_NAME, "complex");
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_TYPE, "oracle");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		testRunner.assertValid();
		testRunner.run();
		testRunner.assertTransferCount(JsonToDDLProcessor.REL_FAILURE, 0);

		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(JsonToDDLProcessor.REL_SUCCESS);
		for (MockFlowFile mockFile : successFiles) {
			try {
				testRunner.assertAllFlowFilesTransferred(JsonToDDLProcessor.REL_SUCCESS);
//				for (String attribute : mockFile.getAttributes().keySet()) {
//					System.out.println("Attribute:" + attribute + "=" + mockFile.getAttribute(attribute));
//				}

				mockFile.assertAttributeExists(JsonToDDLProcessor.FIELD_DDL);
				assertTrue(  mockFile.getAttribute(JsonToDDLProcessor.FIELD_DDL).contains("CREATE TABLE") );
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void processor_should_work_for_inception_fields() {

		try {
			final String filename = "inception.json";
			MockFlowFile flowFile = testRunner.enqueue(new FileInputStream(new File("src/test/resources/" + filename)));
			Map<String, String> attrs = new HashMap<String, String>() {
				{
					put("filename", filename);
				
				}
			};
			flowFile.putAttributes(attrs);			
			testRunner.setValidateExpressionUsage(true);
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_TYPE, "mysql.json");
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_NAME, "${filename:substring( 0, ${filename:length():minus(5)} )}");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		testRunner.assertValid();
		testRunner.run();
		testRunner.assertTransferCount(JsonToDDLProcessor.REL_FAILURE, 0);

		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(JsonToDDLProcessor.REL_SUCCESS);
		for (MockFlowFile mockFile : successFiles) {
			try {
				testRunner.assertAllFlowFilesTransferred(JsonToDDLProcessor.REL_SUCCESS);
//				for (String attribute : mockFile.getAttributes().keySet()) {
//					System.out.println("Attribute:" + attribute + "=" + mockFile.getAttribute(attribute));
//				}

				mockFile.assertAttributeExists(JsonToDDLProcessor.FIELD_DDL);
				assertTrue(  mockFile.getAttribute(JsonToDDLProcessor.FIELD_DDL).contains("CREATE TABLE") );
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void processor_should_work_for_weather_fields() {

		try {
			final String filename = "weather.json";
			MockFlowFile flowFile = testRunner.enqueue(new FileInputStream(new File("src/test/resources/" + filename)));
			Map<String, String> attrs = new HashMap<String, String>() {
				{
					put("filename", filename);				
				}
			};
			flowFile.putAttributes(attrs);
			testRunner.setValidateExpressionUsage(true);
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_NAME, "weather");
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_TYPE, "postgresql");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		testRunner.assertValid();
		testRunner.run();
		testRunner.assertTransferCount(JsonToDDLProcessor.REL_FAILURE, 0);

		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(JsonToDDLProcessor.REL_SUCCESS);
		for (MockFlowFile mockFile : successFiles) {
			try {
				testRunner.assertAllFlowFilesTransferred(JsonToDDLProcessor.REL_SUCCESS);
//				for (String attribute : mockFile.getAttributes().keySet()) {
//					System.out.println("Attribute:" + attribute + "=" + mockFile.getAttribute(attribute));
//				}

				mockFile.assertAttributeExists(JsonToDDLProcessor.FIELD_DDL);
				assertTrue(  mockFile.getAttribute(JsonToDDLProcessor.FIELD_DDL).contains("CREATE TABLE") );
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void processor_should_produce_DDL_with_primary_key(){

		try {
			final String filename = "simple.json";
			MockFlowFile flowFile = testRunner.enqueue(new FileInputStream(new File("src/test/resources/" + filename)));
			Map<String, String> attrs = new HashMap<String, String>() {
				{
					put("filename", filename);
				}
			};
			flowFile.putAttributes(attrs);
			testRunner.setValidateExpressionUsage(false);
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_NAME, "simple");
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_TYPE, "Phoenix");
			testRunner.setProperty(JsonToDDLProcessor.FIELD_PRIMARY_KEY, "emp_id,GENDER");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		testRunner.assertValid();
		testRunner.run();
		testRunner.assertTransferCount(JsonToDDLProcessor.REL_FAILURE, 0);

		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(JsonToDDLProcessor.REL_SUCCESS);
		for (MockFlowFile mockFile : successFiles) {
			try {

				testRunner.assertAllFlowFilesTransferred(JsonToDDLProcessor.REL_SUCCESS);
				for (String attribute : mockFile.getAttributes().keySet()) {
					System.out.println("Attribute:" + attribute + "=" + mockFile.getAttribute(attribute));
				}

				mockFile.assertAttributeExists(JsonToDDLProcessor.FIELD_DDL);
				assertTrue(  mockFile.getAttribute(JsonToDDLProcessor.FIELD_DDL).contains("CREATE TABLE") );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void test_(){
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		JsonNode rootNode = null;
		String json = "{\"data\":11.2}";
		try {
			rootNode = mapper.readTree(json);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(rootNode.fields().next().getValue().isFloat());
	}

	@Test
	public void processor_should_produce_DDL_null(){

		try {
			final String filename = "simple_2.json";
			MockFlowFile flowFile = testRunner.enqueue(new FileInputStream(new File("src/test/resources/" + filename)));
			Map<String, String> attrs = new HashMap<String, String>() {
				{
					put("filename", filename);
				}
			};
			flowFile.putAttributes(attrs);
			testRunner.setValidateExpressionUsage(false);
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_NAME, "simple");
			testRunner.setProperty(JsonToDDLProcessor.FIELD_TABLE_TYPE, "Phoenix");
			testRunner.setProperty(JsonToDDLProcessor.FIELD_PRIMARY_KEY, "OBJECT_KEY,SITE_NUM");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		testRunner.assertValid();
		testRunner.run();
		testRunner.assertTransferCount(JsonToDDLProcessor.REL_FAILURE, 0);

		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(JsonToDDLProcessor.REL_SUCCESS);
		for (MockFlowFile mockFile : successFiles) {
			try {

				testRunner.assertAllFlowFilesTransferred(JsonToDDLProcessor.REL_SUCCESS);
				for (String attribute : mockFile.getAttributes().keySet()) {
					System.out.println("Attribute:" + attribute + "=" + mockFile.getAttribute(attribute));
				}

				mockFile.assertAttributeExists(JsonToDDLProcessor.FIELD_DDL);
				assertTrue(  mockFile.getAttribute(JsonToDDLProcessor.FIELD_DDL).contains("CREATE TABLE") );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}