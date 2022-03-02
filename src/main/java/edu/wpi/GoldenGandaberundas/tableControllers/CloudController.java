package edu.wpi.GoldenGandaberundas.tableControllers;

import static io.grpc.netty.shaded.io.netty.util.internal.ObjectUtil.checkNotNull;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import edu.wpi.GoldenGandaberundas.Main;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import io.grpc.LoadBalancerRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CloudController {

  private Firestore db = null;
  private static CloudController instance = null;

  private CloudController() {
    PickFirstBalancerFactory();

    FirebaseOptions options = null;
    try {
      FileInputStream serviceAccount =
          new FileInputStream(
              new File(
                  Main.class
                      .getResource("gandaberundas-firebase-adminsdk-1gwty-fa1da5db8b.json")
                      .toURI()));

      options =
          new FirebaseOptions.Builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              .build();
    } catch (FileNotFoundException fileNotFoundException) {
      fileNotFoundException.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    FirebaseApp.initializeApp(options);

    db = FirestoreClient.getFirestore();
  }

  public static CloudController getInstance() {
    if (instance == null) {
      synchronized (CloudController.class) {
        if (instance == null) {

          instance = new CloudController();
        }
      }
    }
    return instance;
  }

  public Firestore getDB() {
    return db;
  }

  public <T> void uploadAllDocuments(String collectionName, ArrayList<T> arrayList) {
    Firestore db = CloudController.getInstance().getDB();
    /* If the array is empty, then return nothing */
    if (arrayList.isEmpty()) return;

    /* Get the class type of the objects in the array */
    final Class<?> type = arrayList.get(0).getClass();

    /* Get the name of all the attributes */
    final ArrayList<Field> classAttributes = new ArrayList<>(List.of(type.getDeclaredFields()));

    boolean doesExtend = TableController.class.isAssignableFrom(type);
    if (doesExtend) {
      final Class<?> superType = arrayList.get(0).getClass().getSuperclass();
      classAttributes.addAll(0, (List.of(superType.getDeclaredFields())));
    }

    /* For each object, read each attribute and append it to the file with a comma separating */
    arrayList.forEach(
        obj -> {
          Map<String, Object> docData = new HashMap<>();
          String pk = "";
          for (int i = 0; i < classAttributes.size(); i++) {
            classAttributes.get(i).setAccessible(true);
            try {
              if (i == 0) {
                pk = classAttributes.get(i).get(obj).toString();
              } // else {
              docData.put(classAttributes.get(i).getName(), classAttributes.get(i).get(obj));
              // }
            } catch (IllegalAccessException | ClassCastException e) {
              System.err.println("[firebase addDocument] object attribute access error.");
            }
          }
          DocumentReference docRef = db.collection(collectionName).document(pk);
          ApiFuture<WriteResult> result = docRef.set(docData);
          try {
            result.get();
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
          }
        });
  }

  public Object getEntry(String collection, String pkid, Class<?> classType) {
    try {
      ApiFuture<DocumentSnapshot> doc = db.collection(collection).document(pkid).get();
      return doc.get().toObject(classType);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean editEntry(String collection, String pkid, String colName, Object value) {

    try {
      ApiFuture<WriteResult> future =
          db.collection(collection).document(pkid).update(colName, value);
      System.out.println(future.get());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public ArrayList readTable(String collection, Class objType) {
    try {
      Firestore db = CloudController.getInstance().getDB();

      List<QueryDocumentSnapshot> docs = db.collection(collection).get().get().getDocuments();
      for (QueryDocumentSnapshot q : docs) {
        System.out.println(q.toObject(objType));
      }
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean deleteEntry(String collection, String pkid) {
    ApiFuture<WriteResult> doc = db.collection(collection).document(pkid).delete();
    System.out.println(doc);
    return true;
  }

  public <T> boolean addEntry(String collectionName, T obj) {
    Map<String, Object> docData = new HashMap<>();
    final Class<?> type = obj.getClass();
    final ArrayList<Field> classAttributes = new ArrayList<>(List.of(type.getDeclaredFields()));
    boolean doesExtend = Request.class.isAssignableFrom(type);
    if (doesExtend) {
      final Class<?> superType = obj.getClass().getSuperclass();
      classAttributes.addAll(0, (List.of(superType.getDeclaredFields())));
    }
    String pk = "";
    for (int i = 0; i < classAttributes.size(); i++) {
      classAttributes.get(i).setAccessible(true);
      try {
        if (i == 0) {
          pk = classAttributes.get(0).get(obj).toString();
          if (doesExtend && (obj.getClass() != Request.class)) {
            classAttributes.get(10).setAccessible(true);
            pk += classAttributes.get(10).get(obj).toString();
            System.out.println("PK: " + pk);
          }
        } // else {
        docData.put(classAttributes.get(i).getName(), classAttributes.get(i).get(obj));
        // }
      } catch (IllegalAccessException | ClassCastException e) {
        System.err.println("[firebase addDocument] object attribute access error.");
        return false;
      }
    }
    DocumentReference docRef = db.collection(collectionName).document(pk);
    ApiFuture<WriteResult> result = docRef.set(docData);
    try {
      result.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  private void PickFirstBalancerFactory() {
    var provider =
        checkNotNull(
            LoadBalancerRegistry.getDefaultRegistry().getProvider("pick_first"),
            "pick_first balancer not available");
  }
}
