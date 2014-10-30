# ESID: ElasticSearch Index Dumper

Simple utility to dump out the _source field values from ElasticSearch index(es).

The default output is of a form that can be imported into an ElasticSearch cluster with [ElasticDump](https://github.com/taskrabbit/elasticsearch-dump)

# ElasticSearch Home

In order to build or run `esid` the environment variable `ES_HOME` must be set, pointing to the home directory of your ElasticSearch installation.  We use the lucene and ElasticSearch Java packages from your local ElasticSearch install.  Be sure that you are using the same version of ElasticSearch with ESID that your data was created with.

# Build

```
$ export ES_HOME=<your ElasticSearch install>

$ ant build
```

# Run

```
$ export ES_HOME=<your ElasticSearch install>

$ ./bin/run.sh <index...>
```

The output is of the form:

```
{"_type":"some_document_type_abc", "_id":"some_id_value_123", "_source":{ ...source JSON document... } }
```

which is compatible with ElasticDump.

You can list multiple indexes on the command-line and each will be dumped out in turn, e.g.

```
$ ./bin/run.sh /data/es_indexes/*/*/index
```

# Options

- `-h` help
- `-d` prefix each document output line with the document number
- `-c` emit count of documents in index, rather than dumping the index documents
- `-l` list fields of index with field metadata
