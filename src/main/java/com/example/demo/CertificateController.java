package com.example.demo;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class CertificateController {
	@Autowired
	private SpringTemplateEngine templateEngine;


	@GetMapping("/certificate")
    public ResponseEntity generateCertificate(HttpServletResponse response) throws Exception {
		
		System.out.print("entered");
		
		List<Map<String, String>> metaTags = new ArrayList();

        Map<String, String> metaTag1 = new HashMap();
        metaTag1.put("property", "og:title");
        metaTag1.put("content", "Alignmycareer Course Completion Certificate");
        metaTags.add(metaTag1);

        Map<String, String> metaTag2 = new HashMap<>();
        metaTag2.put("property", "og:url");
        metaTag2.put("content", "https://neerajyadav0.github.io/testing-repo/");
        metaTags.add(metaTag2);

        Map<String, String> metaTag3 = new HashMap<>();
        metaTag3.put("property", "og:description");
        metaTag3.put("content", "my completion certi");
        metaTags.add(metaTag3);

        Map<String, String> metaTag4 = new HashMap<>();
        metaTag4.put("name", "image");
        metaTag4.put("property", "og:image");
        metaTag4.put("content", "https://www.alignmycareer.com/api/v1/file/download?file=candidate/6395d09b766c1d23c5ec347e/photo/images1.jpg");
        metaTags.add(metaTag4);

		
		Context context = new Context();
		context.setVariable("rating", "Expert");
		context.setVariable("assessmentName", "Java");
		context.setVariable("certiImg", "https://www.alignmycareer.com/api/v1/file/download?file=candidate/6395d09b766c1d23c5ec347e/photo/certi1.png");
		context.setVariable("certiDesc", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries");
		context.setVariable("candidatePhoto", "https://img.freepik.com/premium-photo/attractive-businessman-showing-documents-closeup-confident-professional-discuss_723208-1551.jpg");
		context.setVariable("candidateName", "Neeraj Yadav");
		context.setVariable("assessmentDesc", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries");
		context.setVariable("metaTags",metaTags);
		
		String url = new ClassPathResource("/static/").getURL().toString();;


		String html = templateEngine.process("certificate", context);
		String html1 = templateEngine.process("certificate", context);


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html,url);
        renderer.getSharedContext().setBaseURL("templates");
        renderer.layout();
        renderer.createPDF(outputStream);

        // Set response headers
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "inline; filename=converted.pdf");

//         Write PDF content to response
//        StreamUtils.copy(outputStream.toByteArray(), response.getOutputStream());
//        outputStream.close();	
        
//      Convert PDF to image and save
        try (PDDocument document = PDDocument.load(outputStream.toByteArray())) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 300);

                // Save image to storage
                File imageFile = new File("D:\\unstashed folder\\images" + (pageIndex + 1) + ".jpg");
                ImageIO.write(image, "jpg", imageFile);
            }
        }catch (Exception e) {
			System.out.print(e);
		}
//        return templateEngine.process("certificate", context);

//        return "certificate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        // Create a ResponseEntity with the processed HTML and headers
        return new ResponseEntity(html, headers, HttpStatus.OK);

	}
}
		
//        String htmlContent = "<html><body><h1>Hello, World!</h1></body></html>"; // Replace with your HTML content
//
//        // Set up the output image file
//        File outputFile = new File("D:\\unstashed folder\\output.png"); // Replace with your desired output file path
//
//        // Create a temporary PDF file
//        File tempPdfFile = new File("D:\\unstashed folder\\output.pdf");
//        System.out.print("at file location");
//
//
//        try {
//            // Create a ITextRenderer instance and render the HTML to a temporary PDF file
//            ITextRenderer renderer = new ITextRenderer();
//            renderer.setDocumentFromString(htmlContent);
//            renderer.layout();
//            System.out.print("in try ");
//
//
//            // Set up fonts if necessary (optional)
////            ITextFontResolver fontResolver = renderer.getFontResolver();
////            fontResolver.addFont("path/to/font.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); // Replace with your font file path
//
//            try (OutputStream outputStream = new FileOutputStream(tempPdfFile)) {
//                renderer.createPDF(outputStream);
//            } catch (IOException | DocumentException e) {
//                e.printStackTrace();
//            }
//
//            // Convert the PDF to an image
//            BufferedImage image = Graphics2DRenderer.renderToImageAutoSize(tempPdfFile.getAbsolutePath(), 1, 300);
//
//            // Save the image as a PNG file
//            ImageIO.write(image, "png", outputFile);
//            System.out.println("Image saved successfully.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // Delete the temporary PDF file
////            tempPdfFile.delete();
//        }
//    }
//
//	}
	
//		}

