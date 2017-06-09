package bpm.util;

import javafx.concurrent.Task;
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
 * Created by vivek on 09/06/2017.
 */
public class LoadEnvVarFromTWXTask extends Task<Void> {

    private File file;

    private ZipEntry currentZipEntry;
    private ZipFile currentZipFile;
    private Callback callback;

    public LoadEnvVarFromTWXTask(File file, Callback callback) {
        this.file = file;
        this.callback = callback;
    }

    @Override
    public Void call() {
        callback.onComplete(process(file));
        return null;
    }

    private ZipInputStream getCurrentZipStream() throws IOException {
        return new ZipInputStream(currentZipFile.getInputStream(currentZipEntry));
    }

    private List<EnvVarModel> processEnvVarXML(ProjectDetails pd, Document doc) {
        List<EnvVarModel> result = new ArrayList<EnvVarModel>();
        Iterator<Element> envVars = doc.select("envVar").iterator();
        while (envVars.hasNext()) {
            Element ev = envVars.next();
            EnvVarModel temp = pd.getEnvVarModel();
            temp.setName(ev.attr("name"));
            temp.setDefaultValue(ev.select("defaultValue").text());
            Iterator<Element> eds = ev.select("envVarDefault").iterator();
            while (eds.hasNext()) {
                Element ed = eds.next();
                String value = ed.select("value").text();
                String type = ed.select("envTypeId").text();
                if (type.equals("2093.0")) {
                    temp.setDevValue(value);
                } else if (type.equals("2093.1")) {
                    temp.setTestValue(value);
                } else if (type.equals("2093.2")) {
                    temp.setStageValue(value);
                } else if (type.equals("2093.3")) {
                    temp.setProdValue(value);
                }
            }
            result.add(temp);
        }
        return result;
    }

    private ProjectDetails processPackageXML(Document doc) {
        ProjectDetails pd = new ProjectDetails();
        pd.project = doc.select("project").attr("name");
        pd.track = doc.select("branch").attr("name");
        pd.snapshot = doc.select("snapshot").attr("name");
        pd.envFileName = doc.select("object[type=\"environmentVariableSet\"]").attr("id");
        return pd;
    }

    private List<EnvVarModel> processToolkits() {
        String xmlContent = null;
        try {
            xmlContent = Util.getFileNameContainsZip(getCurrentZipStream(), "package.xml");
            Document doc = Jsoup.parse(xmlContent);
            ProjectDetails pd = processPackageXML(doc);
            xmlContent = Util.getFileNameContainsZip(getCurrentZipStream(), pd.envFileName);
            doc = Jsoup.parse(xmlContent);
            return processEnvVarXML(pd, doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<EnvVarModel> process(File file) {
        try {
            List<EnvVarModel> result = new ArrayList<EnvVarModel>();
            ZipFile zf = new ZipFile(file);
            String xmlContent = Util.getFileNameContainsZip(zf, "package.xml");
            Document doc = Jsoup.parse(xmlContent);
            ProjectDetails pd = processPackageXML(doc);
            xmlContent = Util.getFileNameContainsZip(zf, pd.envFileName);
            doc = Jsoup.parse(xmlContent);
            List<EnvVarModel> temp = processEnvVarXML(pd, doc);
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

    public interface Callback {
        void onComplete(List<EnvVarModel> envs);
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

        public EnvVarModel getEnvVarModel() {
            EnvVarModel ev = new EnvVarModel();
            ev.setProject(this.project);
            ev.setSnapshot(this.snapshot);
            ev.setTrack(this.track);
            return ev;
        }
    }


}


