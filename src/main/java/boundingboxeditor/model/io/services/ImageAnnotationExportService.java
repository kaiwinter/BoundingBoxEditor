package boundingboxeditor.model.io.services;

import boundingboxeditor.model.data.ImageAnnotationData;
import boundingboxeditor.model.io.ImageAnnotationSaveStrategy;
import boundingboxeditor.model.io.ImageAnnotationSaver;
import boundingboxeditor.model.io.results.ImageAnnotationExportResult;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.nio.file.Paths;

public class ImageAnnotationExportService extends Service<ImageAnnotationExportResult> {
    final ObjectProperty<File> destination = new SimpleObjectProperty<>(this, "destination");
    final ObjectProperty<ImageAnnotationSaveStrategy.Type>
            exportFormat = new SimpleObjectProperty<>(this, "exportFormat");
    final ObjectProperty<ImageAnnotationData> annotationData = new SimpleObjectProperty<>(this, "imageAnnotationData");
    final ObjectProperty<Runnable> chainedOperation = new SimpleObjectProperty<>(this, "chainedOperation");

    public File getDestination() {
        return destination.get();
    }

    public void setDestination(File destination) {
        this.destination.setValue(destination);
    }

    public ImageAnnotationSaveStrategy.Type getExportFormat() {
        return exportFormat.get();
    }

    public void setExportFormat(ImageAnnotationSaveStrategy.Type exportFormat) {
        this.exportFormat.setValue(exportFormat);
    }

    public ImageAnnotationData getAnnotationData() {
        return annotationData.get();
    }

    public void setAnnotationData(ImageAnnotationData annotationData) {
        this.annotationData.setValue(annotationData);
    }

    public Runnable getChainedOperation() {
        return chainedOperation.get();
    }

    public void setChainedOperation(Runnable chainedOperation) {
        this.chainedOperation.setValue(chainedOperation);
    }

    @Override
    protected Task<ImageAnnotationExportResult> createTask() {
        return new Task<>() {
            @Override
            protected ImageAnnotationExportResult call() throws Exception {
                final ImageAnnotationSaver saver =
                        new ImageAnnotationSaver(exportFormat.get());

                saver.progressProperty()
                     .addListener((observable, oldValue, newValue) -> updateProgress(newValue.doubleValue(), 1.0));

                return saver.save(annotationData.get(), Paths.get(destination.get().getPath()));
            }
        };
    }


}
