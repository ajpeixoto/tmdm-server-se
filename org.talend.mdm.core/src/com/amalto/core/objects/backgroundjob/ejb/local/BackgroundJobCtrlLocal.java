/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.objects.backgroundjob.ejb.local;

/**
 * Local interface for BackgroundJobCtrl.
 * @xdoclet-generated at 7-09-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface BackgroundJobCtrlLocal
   extends javax.ejb.EJBLocalObject
{
   /**
    * Creates or updates a BackgroundJob
    * @throws XtentisException
    */
   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK putBackgroundJob( com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJO backgroundJob ) throws com.amalto.core.util.XtentisException;

   /**
    * Get Background Job
    * @throws XtentisException
    */
   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJO getBackgroundJob( com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK pk ) throws com.amalto.core.util.XtentisException;

   /**
    * Get a BackgroundJob - no exception is thrown: returns null if not found
    * @throws XtentisException
    */
   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJO existsBackgroundJob( com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK pk ) throws com.amalto.core.util.XtentisException;

   /**
    * Remove an Background Job
    * @throws XtentisException
    */
   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK removeBackgroundJob( com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK pk ) throws com.amalto.core.util.XtentisException;

   /**
    * Retrieve all BackgroundJob PKs
    * @throws XtentisException
    */
   public java.util.Collection getBackgroundJobPKs( java.lang.String regex ) throws com.amalto.core.util.XtentisException;

}
