# coding: utf-8

Pod::Spec.new do |s|
  s.name         = "WeexNatMediaImage"
  s.version      = "1.0.0"

  s.summary      = "Nat.js weex plugin: nat-media-image"
  s.description  = <<-DESC
                   Nat.js module for weex: Image
                   DESC

  s.homepage     = "http://natjs.com"
  s.license      = "MIT"
  s.authors      = { "nat" => "hi@natjs.com" }

  s.platform     = :ios, "7.0"

  s.source       = { :git => "https://github.com/natjs/weex-nat-media-image.git", :tag => s.version }
  s.source_files  = "ios/Sources/*.{h,m,mm}"
  
  s.requires_arc = true
  s.dependency "WeexPluginLoader"
  s.dependency "WeexSDK"
  s.dependency "NatImage"
end
