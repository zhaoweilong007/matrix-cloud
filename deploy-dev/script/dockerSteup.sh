yum install -y yum-utils device-mapper-persistent-data lvm2

yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

yum -y install docker-ce-24.0.5


systemctl start docker
systemctl status docker
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
 "registry-mirrors": [
    "https://yogixi6z.mirror.aliyuncs.com",
    "https://dockerproxy.com",
    "https://hub-mirror.c.163.com",
    "https://mirror.baidubce.com",
    "https://ccr.ccs.tencentyun.com"
  ],
  "insecure-registries" : []
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
systemctl enable docker.service


curl -L  https://kgithub.com/docker/compose/releases/download/v2.21.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

chmod +x /usr/local/bin/docker-compose

