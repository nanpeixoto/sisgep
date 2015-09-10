package br.scmba.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
  
public class OperacoesArquivoUtil {  
  
    public static synchronized void downloadFile(String filename, String fileLocation, String mimeType,  
                                                 FacesContext facesContext) {  
  
        ExternalContext context = facesContext.getExternalContext(); // Context  
        String path = fileLocation; // Localizacao do arquivo  
        String fullFileName = path + filename;  
        File file = new File(path); // Objeto arquivo mesmo :)  
  
        HttpServletResponse response = (HttpServletResponse) context.getResponse();  
        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\""); //aki eu seto o header e o nome q vai aparecer na hr do donwload  
        response.setContentLength((int) file.length()); // O tamanho do arquivo  
        response.setContentType(mimeType); // e obviamente o tipo  
  
        try {  
            FileInputStream in = new FileInputStream(file);  
            OutputStream out = response.getOutputStream();  
  
            byte[] buf = new byte[(int)file.length()];  
            int count;  
            while ((count = in.read(buf)) >= 0) {  
                out.write(buf, 0, count);  
            }  
            in.close();  
            out.flush();  
            out.close();  
        facesContext.responseComplete();  
        } catch (IOException ex) {  
            System.out.println("Error in downloadFile: " + ex.getMessage());  
            ex.printStackTrace();  
        }  
    }  
}//By Caio Rodrigo Paulucci  