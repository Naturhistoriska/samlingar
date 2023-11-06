FROM solr:8.1


COPY mycores /opt/solr/server/solr
COPY lib/*.jar /opt/solr/server/solr-webapp/webapp/WEB-INF/lib/




#COPY mycores /opt/solr-8.1.1/server/solr/mycores
#COPY /mycores/nrm/conf/managed-schema /opt/solr/server/solr/configsets/_default/conf/managed-schema
#USER root


#RUN set -x \
#  && curl -o /opt/solr-8.1.1/server/solr-webapp/webapp/WEB-INF/lib/jts-core-1.15.0.jar http://central.maven.org/maven2/org/locationtech/jts/jts-core/1.15.0/jts-core-1.15.0.jar


#USER solr
