package lia.recent;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

public class DeleteTest {
 static final int DOCUMENT_MAX_COUNT = 100;
 public Directory dir = new RAMDirectory();

 @Before
 public void insertDocuments() 
    throws CorruptIndexException, LockObtainFailedException, IOException {
   IndexWriter writer = getWriter();

   for (int i = 0; i < DOCUMENT_MAX_COUNT; i++) {
     Document doc = new Document();
     doc.add(
         new StringField(
         "id",
         Integer.toString(i),
         Field.Store.YES));
     writer.addDocument(doc);
   }
   writer.close();
 }

 private IndexWriter getWriter() 
    throws CorruptIndexException, LockObtainFailedException, IOException {
   IndexWriterConfig conf =
       new IndexWriterConfig( Version.LUCENE_44,
           new WhitespaceAnalyzer(Version.LUCENE_44));
   return new IndexWriter(this.dir, conf);
 }

 private String getIndexFiles() throws IOException {
   String[] list = this.dir.listAll();
   Arrays.sort(list);
   return Arrays.toString(list);
 }


 @Test
 public void testWriterdeletion() throws IOException {
   IndexWriter writer = getWriter();
   assertEquals(DOCUMENT_MAX_COUNT, writer.numDocs());

   assertEquals(
		     "[_0.cfe, _0.cfs, _0.si, segments.gen, segments_1]",
		     getIndexFiles());  
   /***
    * old version

   assertEquals(
     "[_0.fdt, _0.fdx, _0.fnm, _0.frq, _0.nrm, _0.prx, _0.tii, _0.tis, segments.gen, segments_1]",
     getIndexFiles());
    */
   writer.deleteDocuments(new Term("id", "0"));

   assertEquals(
		     "[_0.cfe, _0.cfs, _0.si, segments.gen, segments_1]",
		     getIndexFiles());  

   writer.commit();
   assertEquals(DOCUMENT_MAX_COUNT - 1, writer.numDocs());
   /***
    * old version
    *
   assertEquals(
       "[_0.fdt, _0.fdx, _0.fnm, _0.frq, _0.nrm, _0.prx, _0.tii, _0.tis, _0_1.del, segments.gen, segments_2]",
       getIndexFiles());
 	*/
 }
}
