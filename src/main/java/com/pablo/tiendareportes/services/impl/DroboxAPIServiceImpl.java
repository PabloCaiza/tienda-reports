package com.pablo.tiendareportes.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadBuilder;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.pablo.tiendareportes.services.DroboxAPIService;
import com.pablo.tiendareportes.services.JasperReportsService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@Service
public class DroboxAPIServiceImpl implements DroboxAPIService{

	@Value("${spring.drobox.directorio.reportes}")
	String DIRECTORIO_REPORTES;
	@Value("${spring.drobox.archivo.jrxml}")
	String ARCHIVO_JASPER_JRXML;
	@Autowired
	private JasperReportsService jasperReportService;


	@Override
	public Response descargarReporte(DbxClientV2 dbxClientV2, String orderID, String cliente) {
		ByteArrayOutputStream archivoByte=new ByteArrayOutputStream();
		String mensaje=null;


	
		try {
			

			DbxDownloader<FileMetadata> dowloader= dbxClientV2.files().download(DIRECTORIO_REPORTES+ARCHIVO_JASPER_JRXML);
			dowloader.download(archivoByte);
			mensaje="comrpbante generado correctamente";
			JasperPrint jasperPrint= this.jasperReportService.compilarReporteJasper(archivoByte, orderID);
			this.cargarReporteToDrobox(dbxClientV2, orderID, cliente, jasperPrint);
			
		} catch (DownloadErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return Response.status(Response.Status.OK).encoding(mensaje).build();
	}

	@Override
	public void cargarReporteToDrobox(DbxClientV2 dbxClientV2, String orderID, String cliente,
			JasperPrint jasperPrint) throws IOException, JRException, UploadErrorException, DbxException {
		String nombreArchivoPDF= orderID+".pdf";
		File filePdf=File.createTempFile("temp", nombreArchivoPDF);
		// vamos a agragar informacion al pdf
		InputStream archivoExport=new FileInputStream(filePdf);
		JRPdfExporter jrPdfExporter=new JRPdfExporter();
		jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePdf));
		
		SimplePdfExporterConfiguration simplePdfExporterConfiguration=new SimplePdfExporterConfiguration();
		jrPdfExporter.setConfiguration(simplePdfExporterConfiguration);
		
		jrPdfExporter.exportReport();
		
		UploadBuilder uploadBuilder=dbxClientV2.files().uploadBuilder(DIRECTORIO_REPORTES+"/"+cliente+"/"+nombreArchivoPDF);
		uploadBuilder.withClientModified(new Date(filePdf.lastModified()));
		uploadBuilder.withMode(WriteMode.ADD);
		uploadBuilder.withAutorename(true);
		uploadBuilder.uploadAndFinish(archivoExport);
		archivoExport.close();
		
		
		
		
		
		
	}

}
