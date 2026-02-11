/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import model.entities.Fee;
import org.jdom2.output.Format;

/**
 *
 * @author 50687
 */
public class FeeData {

    private static final String FEE_FILE = "Fees.xml";
    private Element root;
    private Document document;

    public FeeData() throws IOException, JDOMException {
        ensureFileExists();
    }

    private void ensureFileExists() throws IOException, JDOMException {
        File file = new File(FEE_FILE);
        if (!file.exists()) {
            root = new Element("fees");
            document = new Document(root);
            saveToFile();
        } else {
            SAXBuilder builder = new SAXBuilder();
            builder.setIgnoringElementContentWhitespace(true);
            document = builder.build(file);
            root = document.getRootElement();
        }
    }

    private void saveToFile() throws IOException {
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat()); // Para que el XML sea legible
        try (FileWriter writer = new FileWriter(FEE_FILE)) {
            xmlOutput.output(document, writer);
        }
    }

    //Este método sirve para insertar ymodificar todo lo del registro
    public void insertFee(Fee fee) throws IOException {
        deleteFee(fee.getVehicleType());

        Element feeXML = new Element("fee");
        //feeXML.setAttribute("id", String.valueOf(fee.getId()));
        Element feeVehicleType = new Element("vehicleType");
        feeVehicleType.addContent(fee.getVehicleType());
        Element feeHalfHour = new Element("halfHour");
        feeHalfHour.setText(String.valueOf(fee.getHalfHourRate()));
        Element feeHourly = new Element("hourly");
        feeHourly.setText(String.valueOf(fee.getHourlyRate()));
        Element feeDaily = new Element("daily");
        feeDaily.setText(String.valueOf(fee.getDailyRate()));
        Element feeWeekly = new Element("weekly");
        feeWeekly.setText(String.valueOf(fee.getWeeklyRate()));
        Element feeMonthly = new Element("monthly");
        feeMonthly.setText(String.valueOf(fee.getMonthlyRate()));
        Element feeAnnual = new Element("annual");
        feeAnnual.setText(String.valueOf(fee.getAnnualRate()));

        feeXML.addContent(feeVehicleType);
        feeXML.addContent(feeHalfHour);
        feeXML.addContent(feeHourly);
        feeXML.addContent(feeDaily);
        feeXML.addContent(feeWeekly);
        feeXML.addContent(feeMonthly);
        feeXML.addContent(feeAnnual);

        root.addContent(feeXML);
        saveToFile();
    }//insertarEquipo

    public ArrayList<Fee> getAllFees() throws IOException {
        ArrayList<Fee> fees = new ArrayList<>();
        List<Element> feeList = root.getChildren("fee");

        for (Element element : feeList) {
            fees.add(new Fee(
                    element.getChildText("vehicleType"),
                    Float.parseFloat(element.getChildText("halfHour")),
                    Float.parseFloat(element.getChildText("hourly")),
                    Float.parseFloat(element.getChildText("daily")),
                    Float.parseFloat(element.getChildText("weekly")),
                    Float.parseFloat(element.getChildText("monthly")),
                    Float.parseFloat(element.getChildText("annual")))
            );
        }

        return fees;
    }

    //Metodo para mostrar las tarifas de forma individual
    public Fee getFeeByVehicleType(String vehicleType) throws IOException {
        for (Fee fee : getAllFees()) {
            if (fee.getVehicleType().equalsIgnoreCase(vehicleType)) {
                return fee;
            }
        }
        return null;
    }

    public void deleteFee(String vehicleType) throws IOException {
        List<Element> feeList = root.getChildren("fee");
        Element element = null;

        for (Element elementToFound : feeList) {
            if (elementToFound.getChildText("vehicleType").equalsIgnoreCase(vehicleType)) {
                element = elementToFound;
                break;
            }
        }

        if (element != null) {
            root.removeContent(element);
            saveToFile();
        }
    }
}
