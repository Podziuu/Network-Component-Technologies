FROM mongo:8.0.1

COPY keyFile /etc/mongo/keyFile
RUN chown mongodb:mongodb /etc/mongo/keyFile && chmod 400 /etc/mongo/keyFile

COPY mongod.conf /etc/mongod.conf
