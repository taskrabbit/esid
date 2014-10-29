
import java.io.*;
import java.util.*;

import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.document.*;

import org.elasticsearch.index.fieldvisitor.AllFieldsVisitor;


public class IndexDumper
{
  public enum Action { HELP, DUMP, LIST, COUNT };

  public static void main( String[] args ) throws Exception
  {
    if ( args.length < 1 )
      {
        usageAndExit( );
      }

    boolean includeDocIds = false;
    Action action = Action.DUMP;
    List<String> dirs = new ArrayList<String>( );
    int i = 0;
    for ( ; i < args.length; i++ )
      {
             if ( args[i].equals( "-h" ) ) action = Action.HELP;
        else if ( args[i].equals( "-l" ) ) action = Action.LIST;
        else if ( args[i].equals( "-c" ) ) action = Action.COUNT;
        else if ( args[i].equals( "-d" ) ) includeDocIds = true;
        else break;
      }

    if ( Action.HELP == action ) usageAndExit();

    for ( ; i < args.length ; i++ )
      {
        IndexReader reader = DirectoryReader.open( new MMapDirectory( new File( args[i] ) ) );

        switch ( action )
          {
          case LIST:
            listFields( reader );
            break;

          case COUNT:
            countDocs( reader );
            break;

          case DUMP:
            dumpIndex( reader, includeDocIds );
            break;
          }

        reader.close();
      }
  }

  private static void dumpIndex( IndexReader reader, boolean includeDocIds ) throws Exception
  {
    int numDocs = reader.numDocs();

    for ( int i = 0; i < numDocs ; i++ )
      {
        Document doc = reader.document(i);

        if ( includeDocIds )
          {
            System.out.print( i + "\t" );
          }

        AllFieldsVisitor visitor = new AllFieldsVisitor();

        reader.document( i, visitor );

        if ( visitor.uid() != null )
          {
            System.out.print( "{" );
            System.out.print( "\"_type\":\"" + visitor.uid().type() + "\", \"_id\":\"" + visitor.uid().id() + "\"" );
            if ( visitor.source() != null ) System.out.print( ", \"_source\":" + visitor.source().toUtf8() );
            System.out.println( "}" );
          }

      }
  }

  private static void listFields( IndexReader indexReader ) throws Exception
  {
    for ( AtomicReaderContext arc : indexReader.leaves() )
      {
        AtomicReader reader = arc.reader();

        for ( FieldInfo info : reader.getFieldInfos() )
          {
            System.out.println( "info: " + info.number + " " + info.name + " " + info.getIndexOptions() );
          }
      }
  }

  private static void countDocs( IndexReader reader ) throws Exception
  {
    System.out.println( reader.numDocs( ) );
  }

  private static void usageAndExit()
  {
    System.out.println( "Usage: [option] <index...>" );
    System.out.println( "Options:" );
    System.out.println( "  -h            Help" );
    System.out.println( "  -d            Include document numbers in output" );
    System.out.println( "  -c            Emit document count" );
    System.out.println( "  -l            List fields in index" );
    System.exit(1);
  }
}
