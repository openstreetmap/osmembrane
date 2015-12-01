
Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/wily64"
  config.ssh.shell = "bash"
  config.vm.provision "shell", inline: <<SCRIPT
    apt-get install -y openjdk-8-jdk maven
    gem install travis
SCRIPT
end
