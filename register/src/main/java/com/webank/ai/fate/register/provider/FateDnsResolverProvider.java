package com.webank.ai.fate.register.provider;//package com.webank.ai.fate.register.provider;
//
//import com.google.common.base.Preconditions;
//import com.google.common.base.Stopwatch;
//import io.grpc.InternalServiceProviders;
//import io.grpc.NameResolver;
//import io.grpc.NameResolverProvider;
//import io.grpc.internal.DnsNameResolver;
//import io.grpc.internal.GrpcUtil;
//
//import java.net.URI;
//
///**
// * @Description TODO
// * @Author kaideng
// **/
//public class FateDnsResolverProvider  extends NameResolverProvider {
//    private static final String SCHEME = "dns";
//
//    public FateDnsResolverProvider() {
//    }
//
//    @Override
//    public String getDefaultScheme() {
//        return null;
//    }
//
//    @Override
//    protected boolean isAvailable() {
//        return false;
//    }
//
//    @Override
//    protected int priority() {
//        return 0;
//    }
//
//    @Override
//    public DnsNameResolver newNameResolver(URI targetUri, NameResolver.Helper helper) {
//        if ("dns".equals(targetUri.getScheme())) {
//            String targetPath = (String) Preconditions.checkNotNull(targetUri.getPath(), "targetPath");
//            Preconditions.checkArgument(targetPath.startsWith("/"), "the path component (%s) of the target (%s) must start with '/'", targetPath, targetUri);
//            String name = targetPath.substring(1);
//            return new DnsNameResolver(targetUri.getAuthority(), name, helper, GrpcUtil.SHARED_CHANNEL_EXECUTOR, Stopwatch.createUnstarted(), InternalServiceProviders.isAndroid(this.getClass().getClassLoader()));
//        } else {
//            return null;
//        }
//    }
////
////    public String getDefaultScheme() {
////        return "dns";
////    }
////
////    protected boolean isAvailable() {
////        return true;
////    }
////
////    protected int priority() {
////        return 5;
////    }
//}
//
