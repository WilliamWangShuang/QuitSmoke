using System;
using System.Collections;

namespace QuitSmokeWebAPI.Controllers.Entity
{
    public class AuthUser
{
    public string email { get; set; }
    public string password { get; set; }
    public bool returnSecureToken { get; set; }
}
}