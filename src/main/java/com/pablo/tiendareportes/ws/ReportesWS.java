package com.pablo.tiendareportes.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.pablo.tiendareportes.services.DroboxAPIService;

/**
 * 
 * @author pablo WebService que contendra los metodos configurados servicios del
 *         WS
 */

@Component
@Path("/reporteWS")
public class ReportesWS {
	@Value("${spring.drobox.access.token}")
	String ACCESS_TOKEN;
	@Autowired
	private DroboxAPIService droboxAPIServiceImpl;
	
	
	
	@GET
	@Path("/pruebaWS")
	public String pruebaWS() {
		return "ingresando al web service";
	}
	
	
	
	@POST
	@Path("/generarReporte")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response generarReporte(@FormParam("orderID") String orderID, 
			@FormParam("cliente") String cliente,
			@FormParam("destinatario") String destinatario) {
		
	
		DbxRequestConfig config= DbxRequestConfig.newBuilder("drobox/pablo").build();
		DbxClientV2 dbxClientV2 = new DbxClientV2(config, ACCESS_TOKEN);
		Response response=this.droboxAPIServiceImpl.descargarReporte(dbxClientV2, orderID, cliente);
		
		
		return response;

	}

}
