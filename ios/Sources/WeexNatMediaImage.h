//
//  WeexNatMediaImage.h
//
//  Created by huangyake on 17/1/7.
//  Copyright © 2017 Instapp. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <WeexSDK/WeexSDK.h>

@protocol NatMediaImagePro <WXModuleProtocol>

- (void)pick:(NSDictionary *)params :(WXModuleCallback)callback;
- (void)preview:(NSArray *)files :(NSDictionary *)params :(WXModuleCallback)callback;
- (void)info:(NSString *)path :(WXModuleCallback)callback;
- (void)exif:(NSString *)path :(WXModuleCallback)callback;

@end

@interface WeexNatMediaImage : NSObject<NatMediaImagePro>
@end
