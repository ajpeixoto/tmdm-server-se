/* 
 * $Header: /cvsroot/freebuilder/XMLForm/src/org/xmlform/validation/schematron/SchematronFactory.java,v 1.1 2003/03/28 14:11:10 ivelin Exp $
 * $Revision: 1.1 $
 * $Date: 2003/03/28 14:11:10 $
 *
 * ====================================================================
 * This is Open Source Software, distributed
 * under the Apache Software License, Version 1.1
 */

package com.amalto.core.validation.schematron;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.amalto.core.validation.Schema;
import com.amalto.core.validation.SchemaFactory;


/**
 * A helper class which builds a SchematronSchema instance object
 * from a DOM source
 *
 * @author Ivelin Ivanov, ivelin@acm.org, ivelin@iname.com
 * @author Michael Ratliff, mratliff@collegenet.com <mratliff@collegenet.com>, May 2002
 */
public class SchematronFactory extends SchemaFactory
{

  /**
   * the schema name space prefix used in the schema document
   */
  private String schemaPrefix_;

  /**
   * the default schema name space prefix
   */
  private String defaultSchemaPrefix_ = "sch";

  /*
   * private logger
   */
  private Logger logger = setupLogger();


  //
  // Constructors
  //

  /**
   * initialize logger
   */
  protected Logger setupLogger()
  {
    Logger logger = Logger.getLogger( "XmlForm" );
    return logger;
  }

  /**
   * Builds a new Schema instance from
   * the given XML InputSource
   *
   * @param schemaSrc
   *        the Schema document XML InputSource
   */
  public Schema compileSchema(InputSource schemaSrc)
                              throws InstantiationException
  {
    SchematronSchema schema = null;
    try {

      // load Schema file into a DOM document
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ();
      DocumentBuilder dbld = dbf.newDocumentBuilder ();
      Document document = dbld.parse( schemaSrc );

      schema = buildSchema( document );

    } catch (Exception e) {
        logger.log( Level.SEVERE, "!!! Failed loading Schematron schema", e);
        throw new RuntimeException(" !!! Failed loading Schematron schema: \n" + e, e);
    }
   return schema;
  } // build


  /**
   * Build Schematron schema object from a DOM document
   * @param doc DOM document containing the schema
   *
   */
  protected SchematronSchema buildSchema( Document doc )
  {
    SchematronSchema schema = new SchematronSchema();
    boolean errors = false;

    doc.getNamespaceURI ();
    doc.getPrefix ();

    // Initialize the JXPath context
    //Element root = doc.createElement ( "root" );
    Element schemaElement = doc.getDocumentElement ();
    schemaPrefix_ = schemaElement.getPrefix ();
    //root.appendChild (  schemaElement );
    JXPathContext jxpContext = JXPathContext.newContext ( schemaElement );
    jxpContext.setLenient(true);

    // Bind sch:schema element

    // schema title
    String title = (String) jxpContext.getValue ( "title", String.class );
    schema.setTitle( title );
    logger.fine( "Schema title: " + schema.getTitle());

    bindPatterns( schema, jxpContext );
    bindPhases( schema, jxpContext );

    return schema;
  }

  /**
   * populates the patterns elements from the dom tree
   *
   * @param schema the schema instance
   * @param jxpContext
   */
  protected void bindPatterns( SchematronSchema schema, JXPathContext jxpContext)
  {
    // ensure that mandatory elements which are not found
    // will result in Exception
	  jxpContext.setLenient(true);

    // schema patterns
    int ptCount = ((Integer) jxpContext.getValue ( "count(pattern)", Integer.class )).intValue();
    logger.fine( "\nNumber of patterns:  " + ptCount);
    for (int i = 1; i <= ptCount; i++)
    {
      logger.fine( "Pattern# :  " + i);
      Pattern pattern = new Pattern();
      String ptprefix = "pattern[" + i + "]";

      String name = (String) jxpContext.getValue ( ptprefix + "/@name", String.class );
      pattern.setName( name );
      logger.fine( "Pattern name :  " + pattern.getName());
      jxpContext.setLenient(true);
      String id = (String) jxpContext.getValue (  ptprefix + "/@id", String.class );
      pattern.setId( id );
      logger.fine( "Pattern id :  " + pattern.getId() );

      bindRules( pattern, ptprefix, jxpContext );

      schema.addPattern( pattern );
    }
  }


  /**
   * populates the rules elements for a pattern
   * from the dom tree
   *
   * @param pattern
   * @param pathPrefix pattern path prefix
   * @param jxpContext JXPathContext
   */
  protected void bindRules( Pattern pattern, String pathPrefix, JXPathContext jxpContext)
  {
    // ensure that mandatory elements which are not found
    // will result in Exception
    jxpContext.setLenient(false);

    // schema rules
    int ruleCount = ((Integer) jxpContext.getValue ( "count(" + pathPrefix + "/rule)", Integer.class )).intValue();
    logger.fine( "\nNumber of rules:  " + ruleCount);
    for (int i = 1; i <= ruleCount; i++)
    {
      logger.fine( "Rule# :  " + i);
      Rule rule = new Rule();
      String rulePrefix = pathPrefix + "/rule[" + i + "]";

      String context = (String) jxpContext.getValue ( rulePrefix + "/@context", String.class );
      rule.setContext( context );
      logger.fine( "Rule context :  " + rule.getContext());

      bindAsserts( rule, rulePrefix, jxpContext );

      // Patch to make reports work in schematron
      // Note change to name of bindRerports [sic] function
      bindReports( rule, rulePrefix, jxpContext );

      pattern.addRule( rule );
    }
  }


  /**
   * populates the assert elements for a rule
   * from the dom tree
   *
   * @param rule
   * @param pathPrefix rule path prefix
   * @param jxpContext JXPathContext
   */
  protected void bindAsserts( Rule rule, String pathPrefix, JXPathContext jxpContext)
  {
    // ensure that mandatory elements which are not found
    // will result in Exception
    jxpContext.setLenient(false);

    // schema reports
    int elementCount = ((Integer) jxpContext.getValue ( "count(" + pathPrefix + "/assert)", Integer.class )).intValue();
    logger.fine( "\nNumber of asserts:  " + elementCount);
    for (int i = 1; i <= elementCount; i++)
    {
      logger.fine( "Assert# :  " + i);
      Assert assertion = new Assert();
      String assertPrefix = pathPrefix + "/assert[" + i + "]";

      String test = (String) jxpContext.getValue ( assertPrefix + "/@test", String.class );
      assertion.setTest( test );
      logger.fine( "Assert test :  " + assertion.getTest());


      // since diagnostics is a non-mandatory element
      // we will try to get its value in a lenient mode
      jxpContext.setLenient(true);
      String diagnostics = (String) jxpContext.getValue ( assertPrefix + "/@diagnostics", String.class );
      assertion.setDiagnostics( diagnostics );
      logger.fine( "Assert diagnostics :  " + assertion.getDiagnostics());
      jxpContext.setLenient(false);

      
      // now read the report message
      // @todo: The current implementation does not 
      // read xml tags used within the assert message.
      // Solution is to use JXPath NodePointer to get 
      // to the DOM node and then convert it to a String.
      // e.g.
      // NodePointer nptr = (NodePointer) jxpContext.locateValue( assertPrefix );
      // Node msgNode = (Node) nptr.getNodeValue();
      // convery DOMNode to String

      String message = (String) jxpContext.getValue ( assertPrefix, String.class );
      assertion.setMessage( message );
      logger.fine( "Assert message :  " + assertion.getMessage());

      rule.addAssert( assertion );
    }
  }


  /**
   * populates the report elements for a rule
   * from the dom tree
   *
   * @param rule
   * @param pathPrefix rule path prefix
   * @param jxpContext JXPathContext
   */

  protected void bindReports( Rule rule, String pathPrefix, JXPathContext jxpContext)
  {
    // ensure that mandatory elements which are not found
    // will result in Exception
    jxpContext.setLenient(false);

    // schema reports
    int elementCount = ((Integer) jxpContext.getValue ( "count(" + pathPrefix + "/report)", Integer.class )).intValue();
    logger.fine( "\nNumber of reports:  " + elementCount);
    for (int i = 1; i <= elementCount; i++)
    {
      logger.fine( "Report# :  " + i);
      Report report = new Report();
      String assertPrefix = pathPrefix + "/report[" + i + "]";

      String test = (String) jxpContext.getValue ( assertPrefix + "/@test", String.class );
      report.setTest( test );
      logger.fine( "Report test :  " + report.getTest());

      // since diagnostics is a non-mandatory element
      // we will try to get its value in a lenient mode
      jxpContext.setLenient(true);
      String diagnostics = (String) jxpContext.getValue ( assertPrefix + "/@diagnostics", String.class );
      report.setDiagnostics( diagnostics );
      logger.fine( "Report diagnostics :  " + report.getDiagnostics());
      jxpContext.setLenient(false);

      String message = (String) jxpContext.getValue ( assertPrefix, String.class );
      report.setMessage( message );
      logger.fine( "Report message :  " + report.getMessage());

      rule.addReport( report );
    }
  }


  /**
   * populates the phases elements from the dom tree
   *
   * @param schema the schema instance
   * @param jxpContext
   */
  protected void bindPhases( SchematronSchema schema, JXPathContext jxpContext)
  {
    // ensure that mandatory elements which are not found
    // will result in Exception
    jxpContext.setLenient(false);

    // schema phases
    int phaseCount = ((Integer) jxpContext.getValue ( "count(phase)", Integer.class )).intValue();
    logger.fine( "\nNumber of phases:  " + phaseCount);

    for (int i = 1; i <= phaseCount; i++)
    {
      logger.fine( "phase# :  " + i);
      Phase phase = new Phase();
      String phprefix = "phase[" + i + "]";

      String id = (String) jxpContext.getValue ( phprefix + "/@id", String.class );
      phase.setId( id );
      logger.fine( "phase id :  " + phase.getId());

      bindPhaseActivePatterns( phase, phprefix, jxpContext );

      schema.addPhase( phase );
    }
  }


  protected void bindPhaseActivePatterns( Phase phase, String pathPrefix, JXPathContext jxpContext)
  {
    // ensure that mandatory elements which are not found
    // will result in Exception
    jxpContext.setLenient(false);

    // phase active patterns
    int elementCount = ((Integer) jxpContext.getValue ( "count(" + pathPrefix + "/active)", Integer.class )).intValue();
    logger.fine( "Number of active patterns:  " + elementCount);
    for (int i = 1; i <= elementCount; i++)
    {
      logger.fine( "active pattern # :  " + i);
      ActivePattern activePattern = new ActivePattern();
      String assertPrefix = pathPrefix + "/active[" + i + "]";

      String pt = (String) jxpContext.getValue ( assertPrefix + "/@pattern", String.class );
      activePattern.setPattern( pt );
      logger.fine( "Phase active pattern :  " + activePattern.getPattern());

      phase.addActive( activePattern  );
    }
  }




  /*
   * Replace all occurances of sch: with the actual Schema prefix used in the document
   *
   * @todo fix this implementaion. There are problems with DOM.
   * Returns null instead of the actual namespace prefix (e.g. "sch") as expected.
   */
  protected String fixns( String path )
  {
    // Ironicly, at the time I am writing this
    // JDK 1.4 is offering String.replaceAll(regex, str)
    // I don't use it however for backward compatibility
    StringBuffer strbuf = new StringBuffer( path );
    int i = 0;
    int j = 0;
    String dprefix = defaultSchemaPrefix_ + ":";
    int dplen = dprefix.length();
    while ( ( j = path.indexOf ( dprefix, i ) ) >= 0 )
    {
      strbuf.append ( path.substring ( i, j ) );
      strbuf.append ( schemaPrefix_ );
      strbuf.append ( ':' );
      i = j + dplen;
    }
    strbuf.append( path.substring ( i ) );
    return strbuf.toString ();
  }


}

