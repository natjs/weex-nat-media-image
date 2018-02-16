//
//  WeexNatMediaImage.m
//
//  Created by huangyake on 17/1/7.
//  Copyright © 2017 Instapp. All rights reserved.
//

#import "WeexNatMediaImage.h"
#import <WeexPluginLoader/WeexPluginLoader.h>
#import <NatImage/NatImage.h>

#define _FOUR_CC(c1,c2,c3,c4) ((uint32_t)(((c4) << 24) | ((c3) << 16) | ((c2) << 8) | (c1)))
#define _TWO_CC(c1,c2) ((uint16_t)(((c2) << 8) | (c1)))

@implementation WeexNatMediaImage
@synthesize weexInstance;

WX_PlUGIN_EXPORT_MODULE(nat/media/image, WeexNatMediaImage)
WX_EXPORT_METHOD(@selector(pick::))
WX_EXPORT_METHOD(@selector(preview:::))
WX_EXPORT_METHOD(@selector(info::))
WX_EXPORT_METHOD(@selector(exif::))

- (void)pick:(NSDictionary *)params :(WXModuleCallback)callback{
    [[NatImage singletonManger] pick:params :^(id error,id result) {
        if (callback) {
            if (error) {
                callback(error);
            } else {
                callback(result);
            }
        }
    }];
}

- (void)preview:(NSArray *)files :(NSDictionary *)params :(WXModuleCallback)callback{
    [[NatImage singletonManger] preview:files :params :^(id error,id result) {
        if (callback) {
            if (error) {
                callback(error);
            } else {
                callback(result);
            }
        }
    }];
}

- (void)info:(NSString *)path :(WXModuleCallback)callback{
    [[NatImage singletonManger] info:path :^(id error,id result) {
        if (callback) {
            if (error) {
                callback(error);
            } else {
                callback(result);
            }
        }
    }];
}

- (void)exif:(NSString *)path :(WXModuleCallback)callback{
    [[NatImage singletonManger] exif:path :^(id error,id result) {
        if (callback) {
            if (error) {
                callback(error);
            } else {
                callback(result);
            }
        }
    }];
}

@end
