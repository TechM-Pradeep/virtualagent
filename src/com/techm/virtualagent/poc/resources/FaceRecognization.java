package com.techm.virtualagent.poc.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/facerecog")
public class FaceRecognization {

	@POST
	@Consumes("multipart/form-data")
	public String recognizeFace(InputStream inputStream,
			@Context ServletContext context) {
		saveImage(inputStream, context);
		return "hello";
	}

	private void saveImage(InputStream inputStream, ServletContext context) {
		try {
			String rootpath = context.getRealPath("/WEB-INF/");
			rootpath = "D:";
			File outputfile = new File(rootpath + "/image.png");
			@SuppressWarnings("resource")
			OutputStream outStream = new FileOutputStream(outputfile);
			byte[] buffer = new byte[8 * 1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			/*
			 * BufferedImage imBuff = ImageIO.read(inputStream); if (imBuff !=
			 * null) { ImageIO.write(imBuff, "jpg", outputfile); }
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
}
