FROM stain/jena-fuseki

MAINTAINER Ralph Offinger <offinger@hm.edu>

WORKDIR /tmp

# Configuration
ADD config/tdb.ttl /fuseki/configuration/tdb.ttl

WORKDIR /jena-fuseki
EXPOSE 3030
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/jena-fuseki/fuseki-server"]