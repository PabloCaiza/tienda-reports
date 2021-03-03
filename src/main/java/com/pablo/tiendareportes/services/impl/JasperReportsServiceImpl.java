package com.pablo.tiendareportes.services.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pablo.tiendareportes.services.JasperReportsService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class JasperReportsServiceImpl implements JasperReportsService {

	@Value("${spring.datasource.driverClassName}")
	String drive;
	@Value("${spring.datasource.url}")
	String url;
	@Value("${spring.datasource.username}")
	String usuario;
	@Value("${spring.datasource.password}")
	String password;

	@Override
	public JasperPrint compilarReporteJasper(ByteArrayOutputStream archivoBytes, String orderID)
			throws ClassNotFoundException, SQLException, JRException, IOException {
		// obtinen la imagen de logo de nuestro carpeta images
		InputStream image = this.getClass().getClassLoader().getResourceAsStream("images/uce.png");
		// enviar los parametros
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderID);
		map.put("logo", image);
		// conversion el archivo de salida a un flujo de bytes
		byte[] bytes = archivoBytes.toByteArray();
		InputStream archivoInputStream = new ByteArrayInputStream(bytes);

		// se aseigna parametro de conexion para jaspete a la base de datos
		Class.forName(this.drive);
		Connection connection = DriverManager.getConnection(this.url, this.usuario, this.password);
		
		JasperReport jasperReport=JasperCompileManager.compileReport(archivoInputStream);
		image.close();
		archivoInputStream.close();
		
		return JasperFillManager.fillReport(jasperReport, map,connection);
	}

}
