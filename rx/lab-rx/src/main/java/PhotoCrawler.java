import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.Photo;
import model.PhotoSize;
import util.PhotoDownloader;
import util.PhotoProcessor;
import util.PhotoSerializer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoCrawler {

    private static final Logger log = Logger.getLogger(PhotoCrawler.class.getName());

    private final PhotoDownloader photoDownloader;

    private final PhotoSerializer photoSerializer;

    private final PhotoProcessor photoProcessor;

    public PhotoCrawler() throws IOException {
        this.photoDownloader = new PhotoDownloader();
        this.photoSerializer = new PhotoSerializer("./photos");
        this.photoProcessor = new PhotoProcessor();
    }

    public void resetLibrary() throws IOException {
        photoSerializer.deleteLibraryContents();
    }

    public void downloadPhotoExamples() {
        try {
            Observable<Photo> source = photoDownloader.getPhotoExamples();
            source.subscribe(photoSerializer::savePhoto);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Downloading photo examples error", e);
        }
    }

    public void downloadPhotosForQuery(String query) throws IOException {
        try {
            //photoDownloader.searchForPhotos(query).subscribe(photoSerializer::savePhoto);
            /**For resizing*/
            processPhotos(photoDownloader.searchForPhotos(query)).subscribe(photoSerializer::savePhoto);
        } catch (InterruptedException e) {
            log.warning(e.getMessage());
        }
    }

    public void downloadPhotosForMultipleQueries(List<String> queries){
        try {
            photoDownloader.searchForPhotos(queries)
                    .subscribe(photoSerializer::savePhoto);
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }

    public Observable<Photo> processPhotos(Observable<Photo> observable){
        return observable
                .filter(photo -> PhotoSize.resolve(photo) != PhotoSize.SMALL)
                .map(photoProcessor::convertToMiniature);
    }
}
