FROM adoptopenjdk/openjdk15-openj9 as builder

COPY . .

RUN ./gradlew --no-daemon forp-analyze:forp-analyze-serber:installDist

FROM adoptopenjdk/openjdk15-openj9

WORKDIR /user/app

COPY --from=builder forp-analyze/forp-analyze-server/build/install/forp-analyze-server ./

ENTRYPOINT ["/usr/app/bin/forp-analyze-server"]
