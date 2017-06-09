package bpm.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by vivek on 09/06/2017.
 */
public class EnvVarModel {

    private StringProperty project, snapshot, track, name, devValue, defaultValue, testValue, stageValue, prodValue;

    public EnvVarModel() {
        this.project = new SimpleStringProperty();
        this.snapshot = new SimpleStringProperty();
        this.track = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.devValue = new SimpleStringProperty();
        this.defaultValue = new SimpleStringProperty();
        this.testValue = new SimpleStringProperty();
        this.stageValue = new SimpleStringProperty();
        this.prodValue = new SimpleStringProperty();

    }

    public String getProject() {
        return project.get();
    }

    public void setProject(String project) {
        this.project.set(project);
    }

    public StringProperty projectProperty() {
        return project;
    }

    public String getSnapshot() {
        return snapshot.get();
    }

    public void setSnapshot(String snapshot) {
        this.snapshot.set(snapshot);
    }

    public StringProperty snapshotProperty() {
        return snapshot;
    }

    public String getTrack() {
        return track.get();
    }

    public void setTrack(String track) {
        this.track.set(track);
    }

    public StringProperty trackProperty() {
        return track;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDevValue() {
        return devValue.get();
    }

    public void setDevValue(String devValue) {
        this.devValue.set(devValue);
    }

    public StringProperty devValueProperty() {
        return devValue;
    }

    public String getDefaultValue() {
        return defaultValue.get();
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue.set(defaultValue);
    }

    public StringProperty defaultValueProperty() {
        return defaultValue;
    }

    public String getTestValue() {
        return testValue.get();
    }

    public void setTestValue(String testValue) {
        this.testValue.set(testValue);
    }

    public StringProperty testValueProperty() {
        return testValue;
    }

    public String getStageValue() {
        return stageValue.get();
    }

    public void setStageValue(String stageValue) {
        this.stageValue.set(stageValue);
    }

    public StringProperty stageValueProperty() {
        return stageValue;
    }

    public String getProdValue() {
        return prodValue.get();
    }

    public void setProdValue(String prodValue) {
        this.prodValue.set(prodValue);
    }

    public StringProperty prodValueProperty() {
        return prodValue;
    }
}
