package org.envtools.monitor.module.querylibrary.services.export;

import org.envtools.monitor.model.querylibrary.db.Category;
import org.envtools.monitor.model.querylibrary.db.LibQuery;

/**
 * Created by IAvdeev on 18.01.2017.
 */
public class ZipArchiveFilenameConverter {

    /**
     * Replace illegal characters to use title as filename.
     *
     * @param title Title of query
     * @return filename for query with .sql extension
     */
    public static String getFilenameFromQuery(LibQuery query) {
        return replaceInvalidSymbols(query.getTitle()) + ".sql";
    }


    public static String getDirnameFromCategory(Category cat) {
        return replaceInvalidSymbols(cat.getTitle());
    }

    private static String replaceInvalidSymbols(String title) {
        return title.replaceAll("[^a-zA-Z0-9. -]", " ").trim().replaceAll(" {2,}", " ");
    }

}
