package mx.gob.segob.dgtic.web.mvc.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseService {

  /** Intancia para realizar log  */
  protected final Logger logger = LoggerFactory.getLogger(BaseService.class);
  protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();
}
