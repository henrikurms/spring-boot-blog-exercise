
### Schema changes for first-class roles

To support author roles we need to at least store the roles of an author, if we furthermore
want to support custom roles with configurable permission sets we also need to store roles
themselves as well as their permissions.

In order to store the roles of an author we add a `user_role` column to the
`user_post` table as it is already in a many-to-many relationship with both users and posts.

In order to store roles themselves we add a new table `role` that contains the id and name
of the role. The `user_role` in `user` should have foreign key constraint the references this table.

In order to store the permissions of a role we add a table `role_permission` which has a many-to-many
relationship with the `role` table.

It is the assumption here that we want to support a static set of pre-defined set of permissions for ease of implementation
(even though the permissions of an author can change),
in order to support fist-class permissions we would also need to store permissions and some representation of the actions
they allow.

### Fetching and editing blog posts with roles

The changes needed to support roles and permissions depend on the kind of permissions we want to support in the system.

In order to edit a post we need to check that the user is on the authors list of the post with a role that has the editing permission,
and reject the request in case of missing permission.
If we also introduce a read permission we need check it for every post that the user tries to fetch, we can the filter the results to
only the posts that the user is allowed to read.

An owner should be allowed to remove roles from a post except for their own owner role as nobody except admins will be able to
introduce it again. We might allow for a an owner to transfer ownership to somebody else though.