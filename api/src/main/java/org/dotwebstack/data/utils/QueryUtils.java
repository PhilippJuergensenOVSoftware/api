package org.dotwebstack.data.utils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

/**
 * Created by Rick Fleuren on 6/19/2017.
 */
public class QueryUtils {

  public static final String TEMPLATE = "PREFIX %1s: <%2s>\n";

  private static Map<String, String> defaults = new HashMap<>();

  static {
    //w3
    defaults.put("xs", "http://www.w3.org/2001/XMLSchema#");
    defaults.put("rdf", RDF.NAMESPACE);
    defaults.put("rdfs", RDFS.NAMESPACE);
    defaults.put("shacl", "http://www.w3.org/ns/shacl#");
    defaults.put("xsd", "http://www.w3.org/2001/XMLSchema#");
    defaults.put("vcard", "http://www.w3.org/2006/vcard/ns#");
    defaults.put("dcat", "http://www.w3.org/ns/dcat#");
    defaults.put("vcard", "http://www.w3.org/2006/vcard/ns#");

    //iso
    defaults.put("iso", "http://id.loc.gov/vocabulary/iso639-1/");

    //Elmo, used for LDT
    defaults.put("elmo", "http://bp4mc2.org/elmo/def#");

    //Skos:
    defaults.put("skos", "http://www.w3.org/2004/02/skos/core#");
    defaults.put("thes", "http://purl.org/iso25964/skos-thes#");
    defaults.put("skoslex", "http://bp4mc2.org/def/skos-lex/");

    //foaf
    defaults.put("foaf", "http://xmlns.com/foaf/0.1/");

  }

  public static String addDefaultPrefixes(String query) {
    if (query == null) {
      return null;
    }

    for (Map.Entry<String, String> entrySet : defaults.entrySet()) {
      String key = entrySet.getKey();
      if (query.contains(key + ":") && !query.toLowerCase().contains("prefix " + key + ":")) {
        query = String.format(TEMPLATE, key, entrySet.getValue()) + query;
      }
    }
    return query;
  }

  public static String expand(String value) {
    if (value == null) {
      return null;
    }

    if (value.contains(":")) {
      //Ignore URI's
      if (isURI(value)) {
        return value;
      }

      String prefix = value.substring(0, value.indexOf(":"));

      if (!defaults.containsKey(prefix)) {
        throw new IllegalArgumentException("Cannot resolve namespace: " + prefix);
      } else {
        return value.replace(prefix + ":", defaults.get(prefix));
      }
    } else {
      //nothing to expand
      return value;
    }
  }

  public static boolean isURI(String uri) {
    final URL url;
    try {
      url = new URL(uri);
    } catch (Exception e1) {
      return false;
    }
    return true;
  }

}
