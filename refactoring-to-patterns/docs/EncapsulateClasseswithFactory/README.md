![encapsulate class with factory](./Screenshot%20from%202020-09-08%2006-40-05.png)

# Step1
The following example is based on object-to-relational mapping code that is used to write and read objects to and from a relational database.

1. I begin with a small hierarchy of classes that reside in a package called descriptors. These classes assist in mapping database attributes to the instance variables of objects:

package descriptors;

public abstract class AttributeDescriptor...
   protected AttributeDescriptor(...)

public class BooleanDescriptor extends AttributeDescriptor...
   public BooleanDescriptor(...) {
      super(...);
   }

public class DefaultDescriptor extends AttributeDescriptor...
   public DefaultDescriptor(...) {
      super(...);
   }

public class ReferenceDescriptor extends AttributeDescriptor...
   public ReferenceDescriptor(...) {
      super(...);
   }

The abstract AttributeDescriptor constructor is protected, and the constructors for the three subclasses are public. While I'm showing only three subclasses of AttributeDescriptor, there are actually about ten in the real code.

I'll focus on the DefaultDescriptor subclass. The first step is to identify a kind of instance that can be created by the DefaultDescriptor constructor. To do that, I look at some client code:

protected List createAttributeDescriptors() {
   List result = new ArrayList();
   result.add(new DefaultDescriptor("remoteId", getClass(), Integer.TYPE));
   result.add(new DefaultDescriptor("createdDate", getClass(), Date.class));
   result.add(new DefaultDescriptor("lastChangedDate", getClass(), Date.class));
   result.add(new ReferenceDescriptor("createdBy", getClass(), User.class,
      RemoteUser.class));
   result.add(new ReferenceDescriptor("lastChangedBy", getClass(), User.class,
      RemoteUser.class));
   result.add(new DefaultDescriptor("optimisticLockVersion", getClass(), Integer.TYPE));
   return result;
}

Here I see that DefaultDescriptor is being used to represent mappings for Integer and Date types. While it may also be used to map other types, I must focus on one kind of instance at a time. I decide to produce a creation method that will create attribute descriptors for Integer types. I begin by applying Extract Method [F] to produce a public, static creation method called forInteger(…):

protected List createAttributeDescriptors()...
   List result = new ArrayList();
   result.add(
forInteger("remoteId", getClass(), Integer.TYPE));
   ...


public static DefaultDescriptor forInteger(...) {
   
return new DefaultDescriptor(...);

}


Because forInteger(…) always creates AttributeDescriptor objects for an Integer, there is no need to pass it the value Integer.TYPE:

protected List createAttributeDescriptors()...
   List result = new ArrayList();
   result.add(forInteger("remoteId", getClass()

, Integer.TYPE));
   ...

public static DefaultDescriptor forInteger(...) {
   return new DefaultDescriptor(..., 
Integer.TYPE);
}

I also change the forInteger(…) method's return type from DefaultDescriptor to AttributeDescriptor because I want clients to interact with all AttributeDescriptor subclasses via the AttributeDescriptor interface:

public static 
AttributeDescriptor 
DefaultDescriptor forInteger(...)...


Now I move forInteger(…) to the AttributeDescriptor class by applying Move Method [F]:

public abstract class AttributeDescriptor {
   
public static AttributeDescriptor forInteger(...) {
      
return new DefaultDescriptor(...);
   
}


The client code now looks like this:

protected List createAttributeDescriptors()...
   List result = new ArrayList();
   result.add(
AttributeDescriptor.forInteger(...));
   ...

I compile and test to confirm that everything works as expected.

2. Next, I search for all other callers to the DefaultDescriptor constructor that produce an AttributeDescriptor for an Integer, and I update them to call the new creation method:

protected List createAttributeDescriptors() {
   List result = new ArrayList();
   result.add(AttributeDescriptor.forInteger("remoteId", getClass()));
   ...
   result.add(
AttributeDescriptor.forInteger("optimisticLockVersion", getClass()));
   return result;
}

I compile and test. Everything is working.

3. Now I repeat steps 1 and 2 as I continue to produce creation methods for the remaining kinds of instances that the DefaultDescriptor constructor can create. This leads to two more creation methods:

public abstract class AttributeDescriptor {
   public static AttributeDescriptor forInteger(...) {
      return new DefaultDescriptor(...);
   }

   
public static AttributeDescriptor forDate(...) {
      
return new DefaultDescriptor(...);
   
}

   
public static AttributeDescriptor forString(...) {
      
return new DefaultDescriptor(...);
   
}


4. I now declare the DefaultDescriptor constructor protected:

public class DefaultDescriptor extends AttributeDescriptor {
   
protected DefaultDescriptor(...) {
      super(...);
   }

I compile and everything goes according to plan.

5. I repeat steps 1–4 for the other AttributeDescriptor subclasses. When I'm done, the new code

Gives access to AttributeDescriptor subclasses via their superclass.

Ensures that clients obtain subclass instances via the AttributeDescriptor interface.

Prevents clients from directly instantiating AttributeDescriptor subclasses.

Communicates to other programmers that AttributeDescriptor subclasses aren't meant to be public. Clients interact with subclass instances via their common interface.
