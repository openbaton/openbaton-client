class OpenbatonClient < Formula
  desc "Formula of Open Baton Client"
  homepage "http://www.openbaton.org"
  url "https://codeload.github.com/openbaton/openbaton-client/legacy.tar.gz/develop"
  version "3.2.1-SNAPSHOT"
  # sha256 ""

  depends_on :java => "1.7+"
  depends_on "gradle"

  def install
    system "./gradlew", "installDist"
    # Change application path
    inreplace "cli/build/install/cli/bin/openbaton-client", /APP_HOME="`pwd -P`"/, %(APP_HOME="#{libexec}")

    # Remove Windows file
    rm_f Dir["cli/build/install/cli/bin/*.bat"]

    # move the file nfvo.properties in (at default) /usr/local/etc/openbaton/cli/
    openbaton_client_properties_path = etc+"openbaton/cli"
    openbaton_client_properties_path.mkpath
    openbaton_client_properties_path.install "nfvo.properties"

    libexec.install Dir["cli/build/install/cli/*"]
    libexec.install Dir["nfvo.properties"]
    bin.install_symlink Dir["#{libexec}/bin/openbaton-client"]
  end
  test do
    system "false"
  end
end
