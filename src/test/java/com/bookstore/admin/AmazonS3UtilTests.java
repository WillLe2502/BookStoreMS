package com.bookstore.admin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AmazonS3UtilTests {

	@Test
	public void testListFolder() {
		String folderName = "test-upload";
		List<String> listKeys = AmazonS3Util.listFolder(folderName);
		listKeys.forEach(System.out::println);
	}

	@Test
	public void testUploadFile() throws FileNotFoundException {
		String folderName = "test-upload/one";
		String fileName = "1.jpg";
		String filePath = "F:\\Utilities\\" + fileName;

		InputStream inputStream = new FileInputStream(filePath);

		AmazonS3Util.uploadFile(folderName, fileName, inputStream);
	}

	@Test
	public void testDeleteFile() {
		String fileName = "test-upload/1.jpg";
		AmazonS3Util.deleteFile(fileName);
	}

	@Test
	public void testRemoveFolder() {
		String folderName = "test-upload/one";
		AmazonS3Util.removeFolder(folderName);
	}
}