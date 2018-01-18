package com.csy.GrabaTicket.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csy.GrabaTicket.service.IImageService;

@Service
public class ImageServiceImpl implements IImageService {
	
	@Value("${resources.image.directory:/GrabaTicket/image/}")
    private String imageDir;

    @Value("${resources.image.directory.uri:/download}")
    private String imageDirURI;
    
	@Override
	public String upload(byte[] data) {
		OutputStream os = null;
		try {
			File sf=new File(imageDir);
			if(!sf.exists()){    
	           sf.mkdirs();    
			}
		    String imgName = UUID.randomUUID().toString() + ".png";
			os = new FileOutputStream(sf.getPath() + File.separator +  imgName);    
			os.write(data);    	
			return "." + imageDirURI + File.separator + imgName;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
			}
		}
	}

}
