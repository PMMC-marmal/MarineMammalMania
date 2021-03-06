package com.pmmc.app.character;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pmmc.app.screen.Level;

public class B2dContactListener implements ContactListener {

    private final Level parent;

    public B2dContactListener(Level parent){
        this.parent = parent;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        parent.contactor = fa;
        parent.contacting = fb;


        if((fa.getBody().getUserData().equals("Player") && fb.getBody().getUserData().equals("IceBerg")) || (fb.getBody().getUserData().equals("IceBerg") && fa.getBody().getUserData().equals("Player"))){
            parent.isTouchingObstacle = true;
        }
        if((fa.getBody().getUserData().equals("Player") && fb.getBody().getUserData().equals("Sand")) || (fb.getBody().getUserData().equals("Sand") && fa.getBody().getUserData().equals("Player"))){
            parent.isTouchingObstacle = true;
        }
        if((fa.getBody().getUserData().equals("Player") && fb.getBody().getUserData().equals("Bouy")) || (fb.getBody().getUserData().equals("Bouy") && fa.getBody().getUserData().equals("Player"))){
            parent.isTouchingObstacle = true;
        }


    }


    @Override
    public void endContact(Contact contact) {
        parent.isTouchingObstacle = false;
        parent.contacting = null;


    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}