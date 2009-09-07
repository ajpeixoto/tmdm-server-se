/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.objects.routing.v2.ejb.local;

/**
 * Local interface for RoutingRuleCtrl.
 * @xdoclet-generated at 7-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface RoutingRuleCtrlLocal
   extends javax.ejb.EJBLocalObject
{
   /**
    * Creates or updates a menu
    * @throwsXtentisxception 
    */
   public com.amalto.core.objects.routing.v2.ejb.RoutingRulePOJOPK putRoutingRule( com.amalto.core.objects.routing.v2.ejb.RoutingRulePOJO routingRule ) throws com.amalto.core.util.XtentisException;

   /**
    * Get menu
    * @throws XtentisException
    */
   public com.amalto.core.objects.routing.v2.ejb.RoutingRulePOJO getRoutingRule( com.amalto.core.objects.routing.v2.ejb.RoutingRulePOJOPK pk ) throws com.amalto.core.util.XtentisException;

   /**
    * Get a RoutingRule - no exception is thrown: returns null if not found
    * @throws XtentisException
    */
   public com.amalto.core.objects.routing.v2.ejb.RoutingRulePOJO existsRoutingRule( com.amalto.core.objects.routing.v2.ejb.RoutingRulePOJOPK pk ) throws com.amalto.core.util.XtentisException;

   /**
    * Remove a RoutingRule
    * @throws XtentisException
    */
   public com.amalto.core.objects.routing.v2.ejb.RoutingRulePOJOPK removeRoutingRule( com.amalto.core.objects.routing.v2.ejb.RoutingRulePOJOPK pk ) throws com.amalto.core.util.XtentisException;

   /**
    * Retrieve all RoutingRule PKs
    * @throws XtentisException
    */
   public java.util.Collection getRoutingRulePKs( java.lang.String regex ) throws com.amalto.core.util.XtentisException;

}
