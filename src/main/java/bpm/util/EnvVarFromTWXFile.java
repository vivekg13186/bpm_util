package bpm.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by vivek on 08/06/2017.
 */
public class EnvVarFromTWXFile {

    private static ZipEntry currentZipEntry;
    private static ZipFile currentZipFile;

    private static ZipInputStream getCurrentZipStream() throws IOException {
        return new ZipInputStream(currentZipFile.getInputStream(currentZipEntry));
    }

    private static List<EnvVar> processEnvVarXML(ProjectDetails pd, Document doc) {
        List<EnvVar> result = new ArrayList<EnvVar>();
        Iterator<Element> envVars = doc.select("envVar").iterator();
        while (envVars.hasNext()) {
            Element ev = envVars.next();
            EnvVar temp = pd.getEnvVar();
            temp.name = ev.attr("name");
            temp.default_ = ev.select("defaultValue").text();
            Iterator<Element> eds = ev.select("envVarDefault").iterator();
            while (eds.hasNext()) {
                Element ed = eds.next();
                String value = ed.select("value").text();
                String type = ed.select("envTypeId").text();
                if (type.equals("2093.0")) {
                    temp.dev = value;
                } else if (type.equals("2093.1")) {
                    temp.test = value;
                } else if (type.equals("2093.2")) {
                    temp.stage = value;
                } else if (type.equals("2093.3")) {
                    temp.prod = value;
                }
            }
            System.out.println(temp.name);
            result.add(temp);
        }
        return result;
    }

    private static ProjectDetails processPackageXML(Document doc) {
        ProjectDetails pd = new ProjectDetails();
        pd.project = doc.select("project").attr("name");
        pd.track = doc.select("branch").attr("name");
        pd.snapshot = doc.select("snapshot").attr("name");
        pd.envFileName = doc.select("object[type=\"environmentVariableSet\"]").attr("id");
        return pd;
    }

    private static List<EnvVar> processToolkits() {
        String xmlContent = null;
        try {
            xmlContent = Util.getFileNameContainsZip(getCurrentZipStream(), "package.xml");
            Document doc = Jsoup.parse(xmlContent);
            ProjectDetails pd = processPackageXML(doc);
            System.out.print(pd.envFileName);
            xmlContent = Util.getFileNameContainsZip(getCurrentZipStream(), pd.envFileName);
            doc = Jsoup.parse(xmlContent);
            return processEnvVarXML(pd, doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static List<EnvVar> process(File file) {
        try {
            List<EnvVar> result = new ArrayList<EnvVar>();
            ZipFile zf = new ZipFile(file);
            String xmlContent = Util.getFileNameContainsZip(zf, "package.xml");
            Document doc = Jsoup.parse(xmlContent);
            ProjectDetails pd = processPackageXML(doc);
            xmlContent = Util.getFileNameContainsZip(zf, pd.envFileName);
            doc = Jsoup.parse(xmlContent);
            List<EnvVar> temp = processEnvVarXML(pd, doc);
            if (temp != null) {
                result.addAll(temp);
            }


            Enumeration<? extends ZipEntry> et = zf.entries();
            currentZipFile = zf;
            while (et.hasMoreElements()) {
                ZipEntry ze = et.nextElement();
                currentZipEntry = ze;
                if (ze.getName().contains("toolkits")) {
                    temp = processToolkits();
                    if (temp != null) {
                        result.addAll(temp);
                    }
                }
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class ProjectDetails {
        public String project, snapshot, track, envFileName;

        public EnvVar getEnvVar() {
            EnvVar ev = new EnvVar();
            ev.project = this.project;
            ev.snapshot = this.snapshot;
            ev.track = this.track;
            return ev;
        }
    }
}
