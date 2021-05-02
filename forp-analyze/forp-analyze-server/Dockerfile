FROM adoptopenjdk/openjdk15-openj9 as builder

COPY . .

RUN ./gradlew --no-daemon forp-analyze:forp-analyze-server:installDist

FROM adoptopenjdk/openjdk15-openj9

WORKDIR /usr/app

COPY --from=builder forp-analyze/forp-analyze-server/build/install/forp-analyze-server ./

ENTRYPOINT ["bin/forp-analyze-server"]
