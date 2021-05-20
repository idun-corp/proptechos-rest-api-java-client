package com.proptechos.model;

import com.proptechos.model.common.IBaseClass;

import java.util.List;
import java.util.Map;

public class BatchResponse<T> {

    private List<String> saved;

    private List<T> failed;

    private Map<String, String> errors;

    private int totalSaved;

    private int totalFailed;

    private int totalProcessed;

    public List<String> getSaved() {
        return saved;
    }

    public void setSaved(List<String> saved) {
        this.saved = saved;
    }

    public List<T> getFailed() {
        return failed;
    }

    public void setFailed(List<T> failed) {
        this.failed = failed;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public int getTotalSaved() {
        return totalSaved;
    }

    public void setTotalSaved(int totalSaved) {
        this.totalSaved = totalSaved;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public void setTotalFailed(int totalFailed) {
        this.totalFailed = totalFailed;
    }

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }
}
