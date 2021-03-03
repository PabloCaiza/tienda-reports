package com.pablo.tiendareportes.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;

@Component
public class DroboxApiTest {
	
	@Value("${spring.drobox.directorio.reportes}")
	String DIRECTORIO_REPORTES;
	@Value("${spring.drobox.archivo.jrxml}")
	String ARCHIVO_JASPER_JRXML;

	@Test
	public void test() {
		// se configura el token de acceso a la app creando en drobox
		String token = "sl.AsTVaX48jNdxLwbqglrdbU_SIGWqPexu3AGs11QKLGQ1tet6ZnNAwOJlDSIsBl2CLZgvmjgHRiSA35Y34H01A-4GaotwjreI6-In-7018XZZtkIAWl3O_uykgQWtxh_1M9W9iUw";
		//configuracion incicial
		DbxRequestConfig config= DbxRequestConfig.newBuilder("pablo/test-drobox").build();
		//
		DbxClientV2 dbxClientV2=new DbxClientV2(config, token);
		ByteArrayOutputStream archivoByte=new ByteArrayOutputStream();
		DbxDownloader<FileMetadata> dowloader;
		try {
			String direccion=DIRECTORIO_REPORTES+ARCHIVO_JASPER_JRXML;
			System.out.println(direccion);
			dowloader = dbxClientV2.files().download(direccion);
			dowloader.download(archivoByte);
		} catch (DbxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			//obtine informacion de la cuenta perneciente al drobox
			FullAccount fullAccount=dbxClientV2.users().getCurrentAccount();
			System.out.println(fullAccount.getEmail());
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
