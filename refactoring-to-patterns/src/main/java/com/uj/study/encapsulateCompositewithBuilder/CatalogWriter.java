package com.uj.study.encapsulateCompositewithBuilder;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/2 上午6:38
 * @description：
 * @modified By：
 * @version:
 */
public class CatalogWriter {
//    public String catalogXmlFor(Activity activity) {
//        TagNode activityTag = new TagNode("activity");
////    ...
//        TagNode flavorsTag = new TagNode("flavors");
//        activityTag.add(flavorsTag);
//        for (int i=0; i < activity.getFlavorCount(); i++) {
//            TagNode flavorTag = new TagNode("flavor");
//            flavorsTag.add(flavorTag);
//            Flavor flavor = activity.getFlavor(i);
////      ...
//            int requirementsCount = flavor.getRequirements().length;
//            if (requirementsCount > 0) {
//                TagNode requirementsTag = new TagNode("requirements");
//                flavorTag.add(requirementsTag);
//                for (int r=0; r < requirementsCount; r++) {
//                    Requirement requirement = flavor.getRequirements()[r];
//                    TagNode requirementTag = new TagNode("requirement");
////          ...
//                    requirementsTag.add(requirementTag);
//                }
//            }
//        }
//        return activityTag.toString();
//    }

    private String catalogXmlFor(Activity activity) {
        TagBuilder builder = new TagBuilder("activity");

//...

        builder.addChild("flavors");
        for (int i = 0; i < activity.getFlavorCount(); i++) {

            builder.addToParent("flavors", "flavor");
            Flavor flavor = activity.getFlavor(i);
//      ...
            int requirementsCount = flavor.getRequirements().length;
            if (requirementsCount > 0) {

                builder.addChild("requirements");
                for (int r = 0; r < requirementsCount; r++) {
                    Requirement requirement = flavor.getRequirements()[r];

                    builder.addToParent("requirements", "requirement");
//          ...
                }
            }
        }
        return
                builder.toXml();
    }
}
