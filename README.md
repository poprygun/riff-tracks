Why `functions`?  No ports, not servers, no http

## Prerequisites [Follow the instructions from riff site](https://projectriff.io/docs/getting-started/minikube/)

### Install a dfiver

```bash
curl -LO https://storage.googleapis.com/minikube/releases/latest/docker-machine-driver-hyperkit \
&& sudo install -o root -g wheel -m 4755 docker-machine-driver-hyperkit /usr/local/bin/
```

### Create a minikube cluster

```bash
minikube start --memory=4096 --cpus=2 \
--kubernetes-version=v1.13.0 \
--bootstrapper=kubeadm \
--extra-config=apiserver.enable-admission-plugins="LimitRanger,NamespaceExists,NamespaceLifecycle,ResourceQuota,ServiceAccount,DefaultStorageClass,MutatingAdmissionWebhook"
```

### Install riff CLI

```bash
curl -Lo riff-darwin-amd64.tgz https://github.com/projectriff/riff/releases/download/v0.2.0/riff-darwin-amd64.tgz
tar xvzf riff-darwin-amd64.tgz
sudo mv riff /usr/local/bin/
```

```bash
riff system install --node-port
export DOCKER_ID=ashumilov@pivotal.io
riff namespace init default --dockerhub $DOCKER_ID
riff function create node myfunc --git-repo https://github.com/poprygun/mufunc --image gcr.io/${GCP_PROJECT}/myfunc --verbose --artifact square.js

riff service invoke myfunk --json -- -d param <--invoke service directly
```

```bash
kubectl get pods --all-namespaces

watch -n1 kubectl get pod --all-namespaces
watch -n1 kubectl get pods,ksvc,channel,subscription,deploy

kail -d func-00001-deployment -c user-container
```

## Invoke with events

### Create channels - topic that lives on a bus
```bash
riff channel create func-inputs --cluser-bus stub
riff channel create func-outputs --cluser-bus stub
```

### Wire up channels with function
```bash
riff subscription create --channel func-inputs --subscriber square --reply-to func-outputs
```

**Note: `functions` are exposed to public by default, but `channels` are not.**

```bash
riff service create func-service --image projectriff/func-service:s1p2018 <-- figure this out
riff service invoke func-service /func-inputs --json -- -d param -v 
```

### Wire output channel to execute a command line instruction

```bash
riff function create command func-cmd --git-repo https://github.com/poprygun/mufunc-cmd --image gcr.io/${GCP_PROJECT}/myfunc-cmd --verbose --artifact echo.sh

riff service invoke greet --text -- -d "Hi There."

riff channel create func-cmd-replies --cluster-bus stub

riff subscription create --channel func-outputs --subscriber func-cmd --reply-to func-cmd-replies
```

### Send replies back to `func-service`

```bash
riff subscription create --channel func-cmd-replies --subscriber func-service

riff service invoke func-service /func-inputs --json -- -d param -v -H "Knative-Blocking-Request:true"
```

echo 'hi there' | http :8080/tracks
http :8080/processTracks \
    id=7896e496-66ce-3c1a-38e2-cad52335734d \
    latitude=0.47105244400477986 \
    longitude=0.2222105732192745