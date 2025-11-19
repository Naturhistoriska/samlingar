package se.nrm.specify.data.process.logic.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import se.nrm.specify.data.model.impl.Collectionobjectattachment;


/**
 *
 * @author idali
 */
public class AttachmentPredicates {
    
    private final static String IMAGE_CR2 = "image/CR2";
    private final static String OCTET_STREAM = "application/octet-stream"; 
    private final static String PDF = "application/pdf";
    private final static String TEXT_PLAIN = "text/plain";
    private final static String TEXT_RTF = "text/rtf";
    
    private static final Set<String> EXCLUDED_MIME_TYPES =
            new HashSet<>(Arrays.asList(
                    IMAGE_CR2,
                    OCTET_STREAM,
                    PDF,
                    TEXT_PLAIN,
                    TEXT_RTF
            ));

    private AttachmentPredicates() {
        // prevent instantiation
    }

    public static Predicate<Collectionobjectattachment> validPublicAttachment() {
        return (Collectionobjectattachment att) -> att != null &&
                att.getAttachment() != null &&
                att.getAttachment().isIsPublic() &&
                !EXCLUDED_MIME_TYPES.contains(att.getAttachment().getMineType());
    }
}
