package com.pmmc.app.character;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pmmc.app.screen.Level;

public class B2dContactListener implements ContactListener {

    private Level parent;

    public B2dContactListener(Level parent){
        this.parent = parent;
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        System.out.println(fa.getBody().getUserData()+" has hit "+ fb.getBody().getUserData());
        parent.contacting = fb;


        if(fa.getBody().getType() == BodyType.StaticBody){
            parent.isTouchingIceBerg = true;
        }else if(fb.getBody().getType() == BodyType.StaticBody){
            parent.isTouchingIceBerg = true;
        }else{
        }
    }


    @Override
    public void endContact(Contact contact) {
        System.out.println("End Contact");
        parent.isTouchingIceBerg = false;
//        Fixture fa = contact.getFixtureA();
//        Fixture fb = contact.getFixtureB();
        parent.contacting = null;


    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}