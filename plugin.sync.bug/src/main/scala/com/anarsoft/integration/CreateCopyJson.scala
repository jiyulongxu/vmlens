package com.anarsoft.integration

import java.io._;
import scala.collection.mutable.ArrayBuffer
import net.liftweb.json._;
import net.liftweb.json.Extraction._


object CreateCopyJson {
  
  
  def createCopyJson( copyDirPrefix : String  )
  {
    val copyDir = new File(copyDirPrefix + "src/main/resources/copy");
    
    val prefix = "copy/"
    
    val targets = new ArrayBuffer[CopyTarget]
   
    
    for( targetDir <- copyDir.listFiles() )
    {
      if(targetDir.isDirectory())
      {
        targets.append( createTargetDesc(targetDir , prefix));
      }
      
     
    }
    
     val copyDescription  = new CopyDescription(targets.toList);
    
    implicit val formats = DefaultFormats
    
    val stream = new PrintWriter(copyDir.getAbsolutePath() + "/copy.json")
  
    
    
    
    
   stream.println( pretty(render(decompose(copyDescription)))  );
    stream.close();
    
  }
    
  
  
  def createTargetDesc(targetDir : File, prefix : String) =
  {
    
    val targetName =  targetDir.getName();
    val   copyFiles = new ArrayBuffer[CopyFile]
   
    
    
    for(  copyDir <-targetDir.listFiles()  )
    {
      
      
          appendCopyFile(  copyDir ,  prefix + targetName + "/" , copyFiles );
      
      
    
      
    }
    
     val copyTarget  = new CopyTarget( targetName , copyFiles.toList );
    
    
    copyTarget;
    
  }
  
  
  def appendCopyFile(copyDir : File , prefix : String ,  copyFiles :  ArrayBuffer[CopyFile])
  {
    val dirName = copyDir.getName;
    
    for( file <- copyDir.listFiles() )
    {
      
      if( ! file.getName.startsWith(".") )
      {
          val copyFile = new CopyFile( prefix   +  dirName + "/" + file.getName , dirName , file.getName );
          copyFiles.append( copyFile  );
      }
      
      
    }
    
    
    
  }
  
  
  
  
}