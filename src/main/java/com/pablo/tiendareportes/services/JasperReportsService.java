package com.pablo.tiendareportes.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface JasperReportsService {
	/*
	 * permite compilar el arvhico jasper descargado de drobox
	 * */
	
	JasperPrint compilarReporteJasper(ByteArrayOutputStream archivoBytes,String orderID) throws ClassNotFoundException, SQLException, JRException, IOException;
	
	

}
