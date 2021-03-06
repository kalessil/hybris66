//
// HYBPaymentDetails.m
// [y] hybris Platform
//
// Copyright (c) 2000-2016 hybris AG
// All rights reserved.
//
// This software is the confidential and proprietary information of hybris
// ("Confidential Information"). You shall not disclose such Confidential
// Information and shall use it only in accordance with the terms of the
// license agreement you entered into with hybris.
//
// Warning:This file was auto-generated by OCC2Ojbc.
//

#import "HYBPaymentDetails.h"
#import "NSValueTransformer+MTLPredefinedTransformerAdditions.h"

#import "HYBCardType.h"
#import "HYBAddress.h"


@implementation HYBPaymentDetails

+ (instancetype)paymentDetailsWithParams:(NSDictionary*)params {

NSError *error = nil;
HYBPaymentDetails *object = [MTLJSONAdapter modelOfClass:[HYBPaymentDetails class] fromJSONDictionary:params error:&error];

if (error) {
    NSLog(@"Couldn't convert JSON to model HYBPaymentDetails");
    return nil;
}

return object;
}

+ (NSDictionary *)JSONKeyPathsByPropertyKey {
   return @{
@"startMonth" : @"startMonth",
@"saved" : @"saved",
@"issueNumber" : @"issueNumber",
@"cardType" : @"cardType",
@"startYear" : @"startYear",
@"expiryMonth" : @"expiryMonth",
@"expiryYear" : @"expiryYear",
@"accountHolderName" : @"accountHolderName",
@"defaultPayment" : @"defaultPayment",
@"id" : @"id",
@"billingAddress" : @"billingAddress",
@"subscriptionId" : @"subscriptionId",
@"cardNumber" : @"cardNumber"
};
}




+ (NSValueTransformer *)cardTypeJSONTransformer {
return [NSValueTransformer mtl_JSONDictionaryTransformerWithModelClass:[HYBCardType class]];
}

+ (NSValueTransformer *)billingAddressJSONTransformer {
return [NSValueTransformer mtl_JSONDictionaryTransformerWithModelClass:[HYBAddress class]];
}



@end