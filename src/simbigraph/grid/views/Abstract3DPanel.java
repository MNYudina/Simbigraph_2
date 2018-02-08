package simbigraph.grid.views;
/*
import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.utils.universe.*;
*/


import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;


import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

import simbigraph.core.Context;
//import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.grid.model.DefaultGrid;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.model.WrapAroundBorders;
import simbigraph.grid.pseudogrid.SimControlPanel;
import simbigraph.grid.views.Grid2DPanel.TYPEGRID;
import simbigraph.grid.views.panels2d.HexaPanel;
import simbigraph.grid.views.panels2d.SquarePanel;




import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
//import com.sun.j3d.utils.behaviors.vp.*;
//import com.sun.j3d.loaders.Scene;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.Timer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import static java.lang.Math.*;
import java.awt.geom.Arc2D;

//import javolution.util.FastMap;

public class Abstract3DPanel extends JPanel
{
	//Simulation sim;
	private int FRAMERATE = 1000;
	private boolean isTurn = false;
	private double radius = 5;
	private int jScrollBarHOld;
	private int jScrollBarVOld;
	private Color3f objColor= new Color3f(0.0f, 0.0f, 0.0f);
	private Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
	private Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
	private SimpleUniverse univ;
	private BoundingSphere bounds;
	private BranchGroup sceneVariableObjects;///TODO посмотреть можно ли очистить через него!!!!!
	private RotationInterpolator rotator;
	private ExponentialFog ambientFog;
	private AmbientLight ambientLight;
	private AmbientLight ambientSkyLight;
	private DirectionalLight directionalLight1;
	private DirectionalLight directionalLight2;
	private TransformGroup objScaleSky;
	private TransformGroup objScaleVariable;
	private TransformGroup steerTG;
	private Transform3D t3d;
	private Point3d sliderPoint;
	private Point3d sliderPointCenter;
	private Point3d sliderPoint_;
	private Point3d sliderPointCenter_;
	//public static Grid<SimgraphNode> grid;
	private boolean setupVariableObjectsTaskComplite = true;
	
	HashMap<Object,BranchGroup> updateGroup=new HashMap<Object,BranchGroup>();
	/*public static Set<SimgraphNode> updateList=new HashSet<SimgraphNode>();
	public static void addToUpdateList(SimgraphNode feEl) {
		updateList.add(feEl);
	}
*/	
	
	private class NgnTask extends SwingWorker<Void, Void>
	{
		@Override
		public Void doInBackground()
		{
			ngn();
			return null;
		}
		@Override
		public void done()
		{}
	}
	private class SetupVariableObjectsTask extends SwingWorker<Void, Void>
	{
		TransformGroup objScale_;
		int index_;
		SetupVariableObjectsTask(int index)
		{
			//System.out.println("ffff");
			index_ = index;
		}
		@Override
		public Void doInBackground()
		{
			if (index_ == 0)
				;//
			return null;
		}

		@Override
		public void done()
		{}
	}

	private TransformGroup createPrimitive(Node prim, Color color, Point3d xyz, Point3d xayaza)
	{
		Transform3D t3d = new Transform3D();
		Transform3D t3d_ = new Transform3D();
		t3d.set(new Vector3d(xyz.getX(), xyz.getY(), xyz.getZ()));
		t3d_.rotX(xayaza.getX());
		t3d.mul(t3d_);
		t3d_.rotY(xayaza.getY());
		t3d.mul(t3d_);
		t3d_.rotZ(-xayaza.getZ());
		t3d.mul(t3d_);
		TransformGroup tg = new TransformGroup(t3d);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		app.setColoringAttributes(ca);
		objColor = new Color3f(color);

		if (color.getAlpha() < 255)
		{
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency((255 - color.getAlpha()) / 255.0f);
			app.setTransparencyAttributes(ta);
		}

		app.setMaterial(new Material(objColor, black, objColor, white, 80.0f));
		if (prim instanceof Sphere)
			((Sphere)prim).setAppearance(app);
		tg.addChild(prim);

		return tg;
	}
	
	//
	private void movePrimitive(Node prim, Color color, Point3d xyz, Point3d xayaza)
	{
		BranchGroup bg = (BranchGroup)prim;
		TransformGroup tg = (TransformGroup)bg.getChild(0);
		Transform3D t3d = new Transform3D();
		Transform3D t3d_ = new Transform3D();
		t3d.set(new Vector3d(xyz.getX(), xyz.getY(), xyz.getZ()));
		t3d_.rotX(xayaza.getX());
		t3d.mul(t3d_);
		t3d_.rotY(xayaza.getY());
		t3d.mul(t3d_);
		t3d_.rotZ(-xayaza.getZ());
		t3d.mul(t3d_);
		
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		app.setColoringAttributes(ca);
		objColor = new Color3f(color);

		if (color.getAlpha() < 255)
		{
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency((255 - color.getAlpha()) / 255.0f);
			app.setTransparencyAttributes(ta);
		}

		app.setMaterial(new Material(objColor, black, objColor, white, 80.0f));
		if (prim instanceof Sphere)
			((Sphere)prim).setAppearance(app);
		try
		{
			tg.setTransform(t3d);
		}
		catch(javax.media.j3d.BadTransformException ex)
		{
			System.out.println("Primitive BadTransformException");
		}
	}
	//
	private TransformGroup createObject(Scene scene, double scale, Color color, String polygons, Point3d xyz, Point3d xayaza)
	{
		Node obj = scene.getSceneGroup().cloneTree(false);

		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		app.setColoringAttributes(ca);
		objColor = new Color3f(color);

		if (color.getAlpha() < 255)
		{
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency((255 - color.getAlpha()) / 255.0f);
			app.setTransparencyAttributes(ta);
		}

		app.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

		if (!polygons.equalsIgnoreCase(""))
		{
			for (int k = 0; k < polygons.split("_").length; k++)
			{
				int pn = Integer.valueOf(polygons.split("_")[k]);
				((Shape3D)((Group)obj).getChild(pn)).setAppearance(app);
				((Shape3D)((Group)obj).getChild(pn)).setCapability(Shape3D.ALLOW_APPEARANCE_READ);
				((Shape3D)((Group)obj).getChild(pn)).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			}
		}

		Transform3D t3d = new Transform3D();
		Transform3D t3d_ = new Transform3D();

		t3d.set(new Vector3d(xyz.getX(), xyz.getY(), xyz.getZ()));
		t3d.setScale(scale);
		t3d.mul(t3d_);
		t3d_.rotX(PI / 2.);
		t3d.mul(t3d_);
		t3d_.rotY(PI / 2.);
		t3d.mul(t3d_);
		t3d_.rotY(-xayaza.getZ());
		t3d.mul(t3d_);
		TransformGroup tg = new TransformGroup(t3d);

		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.addChild(obj);

		return tg;
	}
	//
	private void moveObject(Node obj, Color color, String polygons, double scale, Point3d xyz, Point3d xayaza)
	{
		BranchGroup bg = (BranchGroup)obj;
		TransformGroup tg = (TransformGroup)bg.getChild(0);

		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		app.setColoringAttributes(ca);
		objColor = new Color3f(color);

		if (color.getAlpha() < 255)
		{
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency((255 - color.getAlpha()) / 255.0f);
			app.setTransparencyAttributes(ta);
		}

		app.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

		if (!polygons.equalsIgnoreCase(""))
		{
			for (int k = 0; k < polygons.split("_").length; k++)
			{
				int pn = Integer.valueOf(polygons.split("_")[k]);
				((Shape3D)((BranchGroup)tg.getChild(0)).getChild(pn)).setAppearance(app);
			}
		}

		Transform3D t3d = new Transform3D();
		Transform3D t3d_ = new Transform3D();
		t3d.set(new Vector3d(xyz.getX(), xyz.getY(), xyz.getZ()));

		t3d.setScale(scale);

		t3d.mul(t3d_);
		t3d_.rotX(PI / 2.);
		t3d.mul(t3d_);
		t3d_.rotY(PI / 2.);
		t3d.mul(t3d_);
		t3d_.rotY(-xayaza.getZ());
		t3d.mul(t3d_);

		try
		{
			tg.setTransform(t3d);
		}
		catch(javax.media.j3d.BadTransformException ex)
		{
			//System.out.println("Object BadTransformException");
		}

		/*if (bg.numChildren() > 1)
		{
			BranchGroup bg_ = (BranchGroup)bg.getChild(1);
			PointSound ps = (PointSound)bg_.getChild(0);
			ps.setPosition((float)xyz.getX(), (float)xyz.getY(), (float)xyz.getZ());
			ps.setSchedulingBounds(new BoundingSphere(xyz, 100));
		}*/
	}
	//
	private void AddMoveObject(Object id, String name, Point3d xyz, double a, Color color, String type)
	{
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		app.setColoringAttributes(ca);
		objColor = new Color3f(color);
		if (color.getAlpha() < 255)
		{
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency((255 - color.getAlpha()) / 255.0f);
			app.setTransparencyAttributes(ta);
		}
		app.setMaterial(new Material(objColor, black, objColor, white, 80.0f));
		boolean ng = false;
	}
	//
	void SetupVariableObjects()
	{
		Grid grid = Context.getGrid();
		int cellSize=30;
		
		//System.out.println("size="+updateList.size());
		for(Object a:grid.getObjects())
		{
			GridPoint pnt=grid.getLocation(a);
			float x,y,z;
			if(hex){
				 x = CommonGraphics.getX(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
				 y = CommonGraphics.getY(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
				 z = CommonGraphics.getZ(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
			}
			else{
				 x = pnt.getX()*cellSize; y= pnt.getY()*cellSize; z= pnt.getZ()*cellSize;
			}
			BranchGroup b=updateGroup.get(a);
			if(b!=null)
			{
				if(hex)
				movePrimitive(b, sim.getAgentColor(a),new Point3d(x - 100,y - 100,z - 2000), new Point3d(0,0,0));
				else
				movePrimitive(b, sim.getAgentColor(a) ,new Point3d(x ,y ,z), new Point3d(0,0,0));

			}
			else{
				if(sim.isVisible(a)){
				BranchGroup objChild = new BranchGroup();
				objChild.setCapability(BranchGroup.ALLOW_DETACH);
				Sphere s = new  Sphere(30);
					if(hex)
						objChild.addChild(createPrimitive(s, sim.getAgentColor(a), new Point3d(x - 100,y - 100,z - 2000), new Point3d(0,0,0)));
					else
						objChild.addChild(createPrimitive(s, sim.getAgentColor(a), new Point3d(x ,y,z), new Point3d(0,0,0)));
				
				objScaleVariable.addChild(objChild);
				updateGroup.put(a, objChild);
				}

			}

		}
		//updateList.clear();
		/*setupVariableObjectsTaskComplite = false;
		
		objScaleVariable.removeAllChildren();
		for(SimgraphNode a:grid.getObjects())
		{
			//SimgraphNode a=grid.getObjects().iterator().next();
			GridPoint pnt=grid.getLocation(a);
			float x = CommonGraphics.getX(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
			float y = CommonGraphics.getY(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
			float z = CommonGraphics.getZ(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
			
			BranchGroup objChild = new BranchGroup();
			objChild.setCapability(BranchGroup.ALLOW_DETACH);
			objChild.addChild(createPrimitive(s, a.getColor(), new Point3d(x - 100,y - 100,z - 2000), new Point3d(0,0,0)));
			objScaleVariable.addChild(objChild);
			updateGroup.put(a, objChild);
		}
		setupVariableObjectsTaskComplite = true;*/
	}
	//
	private void SetupStaticObjects(TransformGroup objScale)
	{
		//ao.getEngine().pause();
		//создать сферу
		int cellSize=50;
		
		
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		app.setColoringAttributes(ca);
		objColor = new Color3f(java.awt.Color.darkGray);
		app.setMaterial(new Material(objColor, black, objColor, white, 80.0f));
		Grid grid = Context.getGrid();
		GridDimensions d=grid.getDimensions();

		
	//	Shape3D shape=Snippet.createGrid(cellSize, java.awt.Color.darkGray, d.getHeight(),d.getWidth(),d.getDepth());
		/*for(SimgraphNode a:grid.getObjects())
			{
				Sphere s = new  Sphere(30);
				GridPoint pnt=grid.getLocation(a);
				float x = CommonGraphics.getX(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
				float y = CommonGraphics.getY(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
				float z = CommonGraphics.getZ(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
				
				objScale.addChild(createPrimitive(s,null,new Point3d(x,y,z),new Point3d(0,0,0)));
			}*/
		
	//	objScale.addChild(shape);
		
		/*Random treeRandomAngle = new Random();
		Random treeRandomOffset = new Random();

		for (double angle = 0; angle < 360; angle += 1)
		{
			double x = (2150 + treeRandomOffset.nextInt(7)) * sin(toRadians(angle));
			double y = (2150 + treeRandomOffset.nextInt(7)) * cos(toRadians(angle));

			xyz = new Point3d(x, y, 0);
			xayaza = new Point3d(0, 0, 0);

			AddStaticObject(objScale, "tree", xyz, treeRandomAngle.nextInt(314159) / 100000., new Color(0, 120, 0));
		}*/
		/*for (double angle = 0.5; angle < 360; angle += 2)
		{
			double x = 2125 * sin(toRadians(angle));
			double y = 2125 * cos(toRadians(angle));

			xyz = new Point3d(x, y, 0);
			xayaza = new Point3d(0, 0, 0);

			AddStaticObject(objScale, "tree", xyz, treeRandomAngle.nextInt(314159) / 100000., new Color(0, 160, 0));
		}
		for (double angle = 1; angle < 360; angle += 2)
		{
			double x = 2100 * sin(toRadians(angle));
			double y = 2100 * cos(toRadians(angle));

			xyz = new Point3d(x, y, 0);
			xayaza = new Point3d(0, 0, 0);

			AddStaticObject(objScale, "tree", xyz, treeRandomAngle.nextInt(314159) / 100000., new Color(0, 200, 0));
		}*/






		/*UgnArrayList<Shape> sral = new UgnArrayList<Shape>();

		if (ao.getEmbeddedObjects() != null)
		{
			for (int i = 0; i < ao.getEmbeddedObjects().size(); i++)
			{
				if (ao.getEmbeddedObjects().get(i) instanceof NetworkResourcePool)
				{
					sral.add(((NetworkResourcePool)ao.getEmbeddedObjects().get(i)).homeNode);
				}

				//if (ao.getEmbeddedObjects().get(i) instanceof NetworkMoveTo)
				//	sral.add(((NetworkMoveTo)ao.getEmbeddedObjects().get(i)).destinationNode(null));

				if (ao.getEmbeddedObjects().get(i) instanceof NetworkSeize)
				{
					sral.add(((NetworkSeize)ao.getEmbeddedObjects().get(i)).destinationNode(null));
				}

				if (ao.getEmbeddedObjects().get(i) instanceof Delay)
				{
					//if (((Delay)ao.getEmbeddedObjects().get(i)).animationGuide instanceof ShapeRectangle)
					sral.add(((Delay)ao.getEmbeddedObjects().get(i)).animationGuide);
				}


				if (ao.getEmbeddedObjects().get(i) instanceof NetworkStorage)
				{
					float zh = 0.2f;
					float zp = 0;
					NetworkStorage ns = (NetworkStorage)ao.getEmbeddedObjects().get(i);

					for (int lvl = 0; lvl < ns.nlevels; lvl++)
					{
						zp = (float)(lvl * 1.5 * ns.positionDepth);

						String s1 = String.valueOf(ns.aisleShape.getX() - 1);
						String s2 = String.valueOf(ns.aisleShape.getY() - ns.positionDepth);
						String s1_ = String.valueOf(ns.aisleShape.getX() - 1);
						String s2_ = String.valueOf(ns.aisleShape.getY() + ns.aisleShape.getHeight() - 1.0 * ns.positionDepth);

						String w = String.valueOf((ns.aisleShape.getWidth() + 2) / 2.);
						String h = String.valueOf(1.0 * ns.positionDepth);

						String a = String.valueOf(ns.aisleShape.getRotation());

						Appearance app2 = new Appearance();
						ColoringAttributes ca2 = new ColoringAttributes();
						app2.setColoringAttributes(ca2);

						if (((NetworkStorage)ao.getEmbeddedObjects().get(i)).aisleShape.getFillColor() != null)
						{
							objColor = new Color3f(ns.aisleShape.getFillColor());

							if (ns.aisleShape.getFillColor().getAlpha() < 255)
							{
								TransparencyAttributes ta = new TransparencyAttributes();
								ta.setTransparencyMode(TransparencyAttributes.NICEST);
								ta.setTransparency((255 - ns.aisleShape.getFillColor().getAlpha()) / 255.0f);
								app2.setTransparencyAttributes(ta);
							}
						}
						else
						{
							objColor = new Color3f(Color.lightGray);
						}

						app2.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

						box = new Box(Float.valueOf(w), Float.valueOf(h), zh, app2);
						Box box_ = new Box(Float.valueOf(w), Float.valueOf(h), zh, app2);

						double alpha = Double.valueOf(a);
						double beta = atan(Float.valueOf(h) / Float.valueOf(w));
						double c = Float.valueOf(h) / sin(beta);

						double bb = c * sin(beta + alpha);
						double aa = c * cos(beta + alpha);

						xyz = new Point3d(Float.valueOf(s1) + aa, -(Float.valueOf(s2) + bb), zh + zp);
						xayaza = new Point3d(0, 0, Float.valueOf(a));

						objScale.addChild(createPrimitive(box, null, xyz, xayaza));

						Point3d xyz_ = new Point3d(Float.valueOf(s1_) + aa, -(Float.valueOf(s2_) + bb), zh + zp);
						objScale.addChild(createPrimitive(box_, null, xyz_, xayaza));
					}
				}
			}
		}




		int i = 0;
		for (int j = 0; j < 10000; j++)
		{
			Object ps = ao.getPersistentShape(i);
			/*if (ps instanceof ShapeOval)
			{
				if (((ShapeOval)ps).getFillColor() != null)
				{
					String name = String.valueOf(((ShapeOval)ps).getName());

					float zh = 40 / 2.f;
					float zp = 0;
					if (name.split("qp").length > 1)
					{
						zp = Float.valueOf(name.split("qp")[1]);
						zh = Float.valueOf(name.split("qp")[2].replace('_', '.')) / 2.f;
					}

					String s1 = String.valueOf(((ShapeRectangle)ps).getX());
					String s2 = String.valueOf(((ShapeRectangle)ps).getY());
					String w = String.valueOf(((ShapeRectangle)ps).getWidth() / 2.);
					String h = String.valueOf(((ShapeRectangle)ps).getHeight() / 2.);
					String a = String.valueOf(((ShapeRectangle)ps).getRotation());

					if (((ShapeRectangle)ps).getGroup() != null)
					{
						s1 = String.valueOf(((ShapeRectangle)ps).getX() + ((ShapeRectangle)ps).getGroup().getX());
						s2 = String.valueOf(((ShapeRectangle)ps).getY() + ((ShapeRectangle)ps).getGroup().getY());
					}

					Appearance app2 = new Appearance();
					ColoringAttributes ca2 = new ColoringAttributes();
					app2.setColoringAttributes(ca2);

					if (((ShapeRectangle)ps).getFillColor() != null)
						objColor = new Color3f(((ShapeRectangle)ps).getFillColor());
					else
						objColor = white;

					if (((ShapeRectangle)ps).getFillColor().getAlpha() < 255)
					{
						TransparencyAttributes ta = new TransparencyAttributes();
						ta.setTransparencyMode(TransparencyAttributes.NICEST);
						ta.setTransparency((255 - ((ShapeRectangle)ps).getFillColor().getAlpha()) / 255.0f);
						app2.setTransparencyAttributes(ta);
					}

					app2.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

					box = new Box(Float.valueOf(w), Float.valueOf(h), zh, app2);

					if (config.hasSection("Static Objects Textures"))
					{
						List<String> permanentObjectsTextures = config.optionNames("Static Objects Textures");
						if (permanentObjectsTextures.size() > 0)
						{
							for (int ii = 0; ii < permanentObjectsTextures.size(); ii++)
							{
								String objectName = permanentObjectsTextures.get(ii);
								String textureName = config.get("Static Objects Textures", objectName);

								if (name.contains(objectName))
								{
									float facesize1 = Float.valueOf(textureName.split(",")[2]);
									float facesize2 = Float.valueOf(textureName.split(",")[3]);

									if (textureName.split(",")[1].equalsIgnoreCase("top"))
										CommonGraphics.BoxAddFaces(box, 0, facesize1, facesize2);

									if (textureName.split(",")[1].equalsIgnoreCase("all"))
									{
										CommonGraphics.BoxAddFaces(box, 0, facesize1, facesize2);
										CommonGraphics.BoxAddFaces(box, 2, facesize1, facesize2);
										CommonGraphics.BoxAddFaces(box, 3, facesize1, facesize2);
										CommonGraphics.BoxAddFaces(box, 4, facesize1, facesize2);
										CommonGraphics.BoxAddFaces(box, 5, facesize1, facesize2);
									}

									TextureLoader tl = new TextureLoader("images/" + textureName.split(",")[0], this);
									if (tl == null)
										System.out.println("Cannot load texture from " + textureName.split(",")[0]);
									else
									{
										//System.out.println("Loaded texture from " + textureName.split(",")[0]);
										TextureAttributes ta = new TextureAttributes();
								 		ta.setTextureMode(TextureAttributes.REPLACE);
								 		app2.setTextureAttributes(ta);
								 		app2.setTexture(tl.getTexture());
									}
									if (textureName.split(",")[1].equalsIgnoreCase("top"))
										box.getShape(0).setAppearance(app2);

									if (textureName.split(",")[1].equalsIgnoreCase("all"))
									{
										box.getShape(0).setAppearance(app2);
										box.getShape(2).setAppearance(app2);
										box.getShape(3).setAppearance(app2);
										box.getShape(4).setAppearance(app2);
										box.getShape(5).setAppearance(app2);
									}
								}
							}
						}
					}

					double alpha = Double.valueOf(a);
					double beta = atan(Float.valueOf(h) / Float.valueOf(w));
					double c = Float.valueOf(h) / sin(beta);

					double bb = c * sin(beta + alpha);
					double aa = c * cos(beta + alpha);

					//objScale.addChild(createObject(box, Float.valueOf(s1) + Float.valueOf(w2), -(Float.valueOf(s2) + Float.valueOf(h2)), 20, Float.valueOf(a)));
					xyz = new Point3d(Float.valueOf(s1) + aa, -(Float.valueOf(s2) + bb), zh + zp);
					xayaza = new Point3d(0, 0, Float.valueOf(a));

					objScale.addChild(createPrimitive(box, null, xyz, xayaza));
				}
			}*/

			/*if (ps instanceof ShapeOval)
			{
				if (((ShapeOval)ps).getFillColor() != null)
				{
					String name = String.valueOf(((ShapeOval)ps).getName());

					float zp = 0;
					if (name.split("qp").length > 1)
						zp = Float.valueOf(name.split("qp")[1]);

					String s1 = String.valueOf(((ShapeOval)ps).getX());
					String s2 = String.valueOf(((ShapeOval)ps).getY());
					String w = String.valueOf(((ShapeOval)ps).getRadiusX());
					String h = String.valueOf(((ShapeOval)ps).getRadiusY());
					String a = String.valueOf(((ShapeOval)ps).getRotation());

					if (((ShapeOval)ps).getGroup() != null)
					{
						s1 = String.valueOf(((ShapeOval)ps).getX() + ((ShapeOval)ps).getGroup().getX());
						s2 = String.valueOf(((ShapeOval)ps).getY() + ((ShapeOval)ps).getGroup().getY());
					}

					Appearance app1 = new Appearance();
					ColoringAttributes ca1 = new ColoringAttributes();
					app1.setColoringAttributes(ca1);

					if (((ShapeOval)ps).getFillColor() != null)
						objColor = new Color3f(((ShapeOval)ps).getFillColor());
					else
						objColor = white;

					if (((ShapeOval)ps).getFillColor().getAlpha() < 255)
					{
						TransparencyAttributes ta = new TransparencyAttributes();
						ta.setTransparencyMode(TransparencyAttributes.NICEST);
						ta.setTransparency((255 - ((ShapeOval)ps).getFillColor().getAlpha()) / 255.0f);
						app1.setTransparencyAttributes(ta);
					}

					app1.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

					Sphere sphere = new Sphere(Float.valueOf(w), app1);

					xyz = new Point3d(Float.valueOf(s1), -Float.valueOf(s2), Float.valueOf(w) + zp);
					xayaza = new Point3d(0, 0, Float.valueOf(a));

					objScale.addChild(createPrimitive(sphere, null, xyz, xayaza));
				}
			}
			

			if (ps instanceof ShapeRectangle)
			{
				if (((ShapeRectangle)ps).getFillColor() != null && sral.indexOf((ShapeRectangle)ps) == -1)
				{
					String name = ((ShapeRectangle)ps).getName();

					double zp = 0;
					double zh = 1;
					
					if (config.hasSection("2D To 3D"))
					{
						List<String> permanentObjects = config.optionNames("2D To 3D");
						if (permanentObjects.size() > 0)
						{
							for (int ii = 0; ii < permanentObjects.size(); ii++)
							{
								String objectName = permanentObjects.get(ii);
								String objectValue = config.get("2D To 3D", objectName);

								if (name.equalsIgnoreCase(objectName))
								{
									zp = Double.valueOf(objectValue.split(",")[0]);
									zh = Double.valueOf(objectValue.split(",")[1]) / 2.0;
									
									double x = ((ShapeRectangle)ps).getX();
									double y = ((ShapeRectangle)ps).getY();
									double w = ((ShapeRectangle)ps).getWidth() / 2.0;
									double h = ((ShapeRectangle)ps).getHeight() / 2.0;
									double a = ((ShapeRectangle)ps).getRotation();

									if (((ShapeRectangle)ps).getGroup() != null)
									{
										x = ((ShapeRectangle)ps).getX() + ((ShapeRectangle)ps).getGroup().getX();
										y = ((ShapeRectangle)ps).getY() + ((ShapeRectangle)ps).getGroup().getY();
									}

									Appearance app2 = new Appearance();
									ColoringAttributes ca2 = new ColoringAttributes();
									app2.setColoringAttributes(ca2);

									objColor = new Color3f(((ShapeRectangle)ps).getFillColor());

									if (((ShapeRectangle)ps).getFillColor().getAlpha() < 255)
									{
										TransparencyAttributes ta2 = new TransparencyAttributes();
										ta2.setTransparencyMode(TransparencyAttributes.NICEST);
										ta2.setTransparency((255 - ((ShapeRectangle)ps).getFillColor().getAlpha()) / 255.0f);
										app2.setTransparencyAttributes(ta2);
									}

									app2.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

									box = new Box((float)w, (float)h, (float)zh, app2);

									if (config.hasSection("Static Objects Textures"))
									{
										List<String> permanentObjectsTextures = config.optionNames("Static Objects Textures");
										if (permanentObjectsTextures.size() > 0)
										{
											for (int iii = 0; iii < permanentObjectsTextures.size(); iii++)
											{
												String objectName_ = permanentObjectsTextures.get(iii);
												String textureName = config.get("Static Objects Textures", objectName_);

												if (name.equalsIgnoreCase(objectName_))
												{
													float facesize1 = Float.valueOf(textureName.split(",")[2]);
													float facesize2 = Float.valueOf(textureName.split(",")[3]);

													if (textureName.split(",")[1].equalsIgnoreCase("top"))
													{
														CommonGraphics.BoxAddFaces(box, 0, facesize1, facesize2);
													}

													if (textureName.split(",")[1].equalsIgnoreCase("all"))
													{
														CommonGraphics.BoxAddFaces(box, 0, facesize1, facesize2);
														CommonGraphics.BoxAddFaces(box, 2, facesize1, facesize2);
														CommonGraphics.BoxAddFaces(box, 3, facesize1, facesize2);
														CommonGraphics.BoxAddFaces(box, 4, facesize1, facesize2);
														CommonGraphics.BoxAddFaces(box, 5, facesize1, facesize2);
													}

													TextureLoader tl = new TextureLoader("images/" + textureName.split(",")[0], this);
													if (tl == null)
													{
														System.out.println("Cannot load texture from " + textureName.split(",")[0]);
													}
													else
													{
														//System.out.println("Loaded texture from " + textureName.split(",")[0]);
														TextureAttributes ta2 = new TextureAttributes();
														ta2.setTextureMode(TextureAttributes.REPLACE);
														app2.setTextureAttributes(ta2);
														app2.setTexture(tl.getTexture());
													}
													if (textureName.split(",")[1].equalsIgnoreCase("top"))
													{
														box.getShape(0).setAppearance(app2);
													}

													if (textureName.split(",")[1].equalsIgnoreCase("all"))
													{
														box.getShape(0).setAppearance(app2);
														box.getShape(2).setAppearance(app2);
														box.getShape(3).setAppearance(app2);
														box.getShape(4).setAppearance(app2);
														box.getShape(5).setAppearance(app2);
													}
												}
											}
										}
									}

									double alpha = a;
									double beta = atan(h / w);
									double c = h / sin(beta);

									double bb = c * sin(beta + alpha);
									double aa = c * cos(beta + alpha);

									//objScale.addChild(createObject(box, Float.valueOf(s1) + Float.valueOf(w2), -(Float.valueOf(s2) + Float.valueOf(h2)), 20, Float.valueOf(a)));
									xyz = new Point3d(x + aa, -(y + bb), zh + zp);
									xayaza = new Point3d(0, 0, a);

									objScale.addChild(createPrimitive(box, null, xyz, xayaza));
									
									break;
								}
							}
						}
					}
				}
			}

			if (ps instanceof ShapeOval)
			{
				if (((ShapeOval)ps).getFillColor() != null)
				{
					String name = String.valueOf(((ShapeOval)ps).getName());

					double zp = 0;
					double zh = 1;
					
					if (config.hasSection("2D To 3D"))
					{
						List<String> permanentObjects = config.optionNames("2D To 3D");
						if (permanentObjects.size() > 0)
						{
							for (int ii = 0; ii < permanentObjects.size(); ii++)
							{
								String objectName = permanentObjects.get(ii);
								String objectValue = config.get("2D To 3D", objectName);

								if (name.equalsIgnoreCase(objectName))
								{
									zp = Double.valueOf(objectValue.split(",")[0]);
									zh = Double.valueOf(objectValue.split(",")[1]);
									
									double x = ((ShapeOval)ps).getX();
									double y = ((ShapeOval)ps).getY();
									double w = ((ShapeOval)ps).getRadiusX();
									double h = ((ShapeOval)ps).getRadiusY();
									double a = ((ShapeOval)ps).getRotation();

									if (((ShapeOval)ps).getGroup() != null)
									{
										x = ((ShapeOval)ps).getX() + ((ShapeOval)ps).getGroup().getX();
										y = ((ShapeOval)ps).getY() + ((ShapeOval)ps).getGroup().getY();
									}

									Appearance app1 = new Appearance();
									ColoringAttributes ca1 = new ColoringAttributes();
									app1.setColoringAttributes(ca1);

									objColor = new Color3f(((ShapeOval)ps).getFillColor());

									if (((ShapeOval)ps).getFillColor().getAlpha() < 255)
									{
										TransparencyAttributes ta = new TransparencyAttributes();
										ta.setTransparencyMode(TransparencyAttributes.NICEST);
										ta.setTransparency((255 - ((ShapeOval)ps).getFillColor().getAlpha()) / 255.0f);
										app1.setTransparencyAttributes(ta);
									}

									app1.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

									Cylinder sphere = new Cylinder((float)w,(float)zh, app1);

									xyz = new Point3d((float)x, -(float)y, (float)zh / 2.0 + zp);
									xayaza = new Point3d(PI / 2., 0, (float)a);

									objScale.addChild(createPrimitive(sphere, null, xyz, xayaza));
									
									break;
								}
							}
						}
					}
				}
			}
			
			if (ps instanceof ShapeLine)
			{
				String name = ((ShapeLine)ps).getName();
				ShapeLine sl = (ShapeLine)ps;
				
				LineArray lineParts = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3 | LineArray.BY_REFERENCE);
				float[] cs = new float[6];

				double zp = 0;
				double zh = 1;
				
				if (config.hasSection("2D To 3D"))
				{
					List<String> permanentObjects = config.optionNames("2D To 3D");
					if (permanentObjects.size() > 0)
					{
						for (int ii = 0; ii < permanentObjects.size(); ii++)
						{
							String objectName = permanentObjects.get(ii);
							String objectValue = config.get("2D To 3D", objectName);

							if (name.equalsIgnoreCase(objectName))
							{
								zp = Double.valueOf(objectValue.split(",")[0]);
								zh = Double.valueOf(objectValue.split(",")[1]);
								
								cs[0] = (float)sl.getX();
								cs[1] = -(float)sl.getY();
								cs[2] = (float)zp;
								cs[3] = (float)sl.getEndX();
								cs[4] = -(float)sl.getEndY();
								cs[5] = (float)zp;

								Color3f col = new Color3f(sl.getColor());
								float[] cols = new float[6];
								cols[0] = col.x;
								cols[1] = col.y;
								cols[2] = col.z;
								cols[3] = col.x;
								cols[4] = col.y;
								cols[5] = col.z;
								
								lineParts.setCoordRefFloat(cs);
								lineParts.setColorRefFloat(cols);
								
								Shape3D n = new Shape3D();
								n.setGeometry(lineParts);
								
								
								Appearance app1 = new Appearance();
								ColoringAttributes ca1 = new ColoringAttributes();
								app1.setColoringAttributes(ca1);

								objColor = new Color3f(sl.getColor());

								if (sl.getColor().getAlpha() < 255)
								{
									TransparencyAttributes ta = new TransparencyAttributes();
									ta.setTransparencyMode(TransparencyAttributes.NICEST);
									ta.setTransparency((255 - sl.getColor().getAlpha()) / 255.0f);
									app1.setTransparencyAttributes(ta);
								}

								app1.setMaterial(new Material(objColor, black, objColor, white, 80.0f));
								
								
								LineAttributes la = new LineAttributes();
								la.setLineAntialiasingEnable(true);
								la.setLineWidth((float)sl.getLineWidth());
								
								app1.setLineAttributes(la);
								n.setAppearance(app1);

								objScale.addChild(n);
								
								break;
							}
						}
					}
				}
			}
			
			if (ps instanceof ShapePolyLine)
			{
				if (((ShapePolyLine)ps).getFillColor() != null)
				{
					String name = ((ShapePolyLine)ps).getName();

					double zp = 0;
					double zh = 1;
					
					if (config.hasSection("2D To 3D"))
					{
						List<String> permanentObjects = config.optionNames("2D To 3D");
						if (permanentObjects.size() > 0)
						{
							for (int ii = 0; ii < permanentObjects.size(); ii++)
							{
								String objectName = permanentObjects.get(ii);
								String objectValue = config.get("2D To 3D", objectName);

								if (name.equalsIgnoreCase(objectName))
								{
									zp = Double.valueOf(objectValue.split(",")[0]);
									zh = Double.valueOf(objectValue.split(",")[1]);
									
									double s1 = ((ShapePolyLine)ps).getX();
									double s2 = ((ShapePolyLine)ps).getY();

									Appearance app1 = new Appearance();
									ColoringAttributes ca1 = new ColoringAttributes();
									app1.setColoringAttributes(ca1);

									objColor = new Color3f(((ShapePolyLine)ps).getFillColor());

									if (((ShapePolyLine)ps).getFillColor().getAlpha() < 255)
									{
										TransparencyAttributes ta = new TransparencyAttributes();
										ta.setTransparencyMode(TransparencyAttributes.NICEST);
										ta.setTransparency((255 - ((ShapePolyLine)ps).getFillColor().getAlpha()) / 255.0f);
										app1.setTransparencyAttributes(ta);
									}

									app1.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

									if (config.hasSection("Static Objects Textures"))
									{
										List<String> permanentObjectsTextures = config.optionNames("Static Objects Textures");
										if (permanentObjectsTextures.size() > 0)
										{
											for (int iii = 0; iii < permanentObjectsTextures.size(); iii++)
											{
												String objectName_ = permanentObjectsTextures.get(iii);
												String textureName = config.get("Static Objects Textures", objectName_);

												if (name.equalsIgnoreCase(objectName_))
												{
													float facesize1 = Float.valueOf(textureName.split(",")[2]);
													float facesize2 = Float.valueOf(textureName.split(",")[3]);


													TextureLoader tl = new TextureLoader("images/" + textureName.split(",")[0], this);
													if (tl == null)
													{
														System.out.println("Cannot load texture from " + textureName.split(",")[0]);
													}
													else
													{
														//System.out.println("Loaded texture from " + textureName.split(",")[0]);
														TextureAttributes ta = new TextureAttributes();
														ta.setTextureMode(TextureAttributes.MODULATE);
														app1.setTextureAttributes(ta);
														app1.setTexture(tl.getTexture());
													}
												}
											}
										}
									}


									java.awt.geom.GeneralPath gp = new java.awt.geom.GeneralPath(java.awt.geom.GeneralPath.WIND_NON_ZERO);
									gp.moveTo(0, 0);
									for (int k = 1; k < ((ShapePolyLine)ps).getNPoints(); k++)
									{
										gp.lineTo(((ShapePolyLine)ps).getPointDx(k), - ((ShapePolyLine)ps).getPointDy(k));
									}
									gp.closePath();

									CommonGraphics.AWTShapeExtrusion extrusion = new CommonGraphics.AWTShapeExtrusion((float)zh);
									CommonGraphics.AWTShapeExtruder extruder = new CommonGraphics.AWTShapeExtruder(0.1, extrusion);

									GeometryArray geom = extruder.getGeometry(gp, null);

									Shape3D shape3D = new Shape3D();
									shape3D.setGeometry(geom);
									shape3D.setAppearance(app1);

									xyz = new Point3d((float)s1, -(float)s2, zp);
									xayaza = new Point3d(0, 0, 0);

									objScale.addChild(createPrimitive(shape3D, null, xyz, xayaza));
									
									break;
								}
							}
						}
					}
				}
				else
				{
					String name = ((ShapePolyLine)ps).getName();

					double zp = 0;
					double zh = 1;
					
					if (config.hasSection("2D To 3D"))
					{
						List<String> permanentObjects = config.optionNames("2D To 3D");
						if (permanentObjects.size() > 0)
						{
							for (int ii = 0; ii < permanentObjects.size(); ii++)
							{
								String objectName = permanentObjects.get(ii);
								String objectValue = config.get("2D To 3D", objectName);

								if (name.equalsIgnoreCase(objectName))
								{
									zp = Double.valueOf(objectValue.split(",")[0]);
									zh = Double.valueOf(objectValue.split(",")[1]);
									
									for (int k = 0; k < ((ShapePolyLine)ps).getNPoints() - 1; k++)
									{
										LineArray lineParts = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3 | LineArray.BY_REFERENCE);
										float[] cs = new float[6];
										int z = 0;
										for (int kk = 0; kk < 6; kk += 3)
										{
											cs[kk] = (float)((ShapePolyLine)ps).getX() + (float)((ShapePolyLine)ps).getPointDx(k + z);
											cs[kk + 1] = -(float)((ShapePolyLine)ps).getY() - (float)((ShapePolyLine)ps).getPointDy(k + z);
											cs[kk + 2] = (float)zp;
											
											z++;
										}
										Color3f col = new Color3f(((ShapePolyLine)ps).getLineColor());
										float[] cols = new float[6];
										cols[0] = col.x;
										cols[1] = col.y;
										cols[2] = col.z;
										cols[3] = col.x;
										cols[4] = col.y;
										cols[5] = col.z;
										
										lineParts.setCoordRefFloat(cs);
										lineParts.setColorRefFloat(cols);
										
										Shape3D n = new Shape3D();
										n.setGeometry(lineParts);
										
										Appearance app1 = new Appearance();
										ColoringAttributes ca1 = new ColoringAttributes();
										app1.setColoringAttributes(ca1);

										objColor = new Color3f(((ShapePolyLine)ps).getLineColor());

										if (((ShapePolyLine)ps).getLineColor().getAlpha() < 255)
										{
											TransparencyAttributes ta = new TransparencyAttributes();
											ta.setTransparencyMode(TransparencyAttributes.NICEST);
											ta.setTransparency((255 - ((ShapePolyLine)ps).getLineColor().getAlpha()) / 255.0f);
											app1.setTransparencyAttributes(ta);
										}

										app1.setMaterial(new Material(objColor, black, objColor, white, 80.0f));
										
										
										LineAttributes la = new LineAttributes();
										la.setLineAntialiasingEnable(true);
										la.setLineWidth((float)((ShapePolyLine)ps).getLineWidth());
										
										app1.setLineAttributes(la);
										n.setAppearance(app1);

										objScale.addChild(n);
									}
									
									break;
								}
							}
						}
					}
				}
			}

			
			if (ps instanceof ShapePixel)
			{
				if (((ShapePixel)ps).getColor() != null)
				{
					String name = ((ShapePixel)ps).getName();

					if (config.hasSection("Replace With Object"))
					{
						List<String> permanentObjects = config.optionNames("Replace With Object");
						if (permanentObjects.size() > 0)
						{
							for (int ii = 0; ii < permanentObjects.size(); ii++)
							{
								String objectName = permanentObjects.get(ii);
								String objectValue = config.get("Replace With Object", objectName);

								if (name.equalsIgnoreCase(objectName))
								{
									//zp = Double.valueOf(objectValue.split(",")[0]);
									//zh = Double.valueOf(objectValue.split(",")[1]) / 2.0;

									double x = ((ShapePixel)ps).getX();
									double y = ((ShapePixel)ps).getY();
									double a = ((ShapePixel)ps).getRotation();

									if (((ShapePixel)ps).getGroup() != null)
									{
										x = ((ShapePixel)ps).getX() + ((ShapePixel)ps).getGroup().getX();
										y = ((ShapePixel)ps).getY() + ((ShapePixel)ps).getGroup().getY();
									}

									Appearance app2 = new Appearance();
									ColoringAttributes ca2 = new ColoringAttributes();
									app2.setColoringAttributes(ca2);

									objColor = new Color3f(((ShapePixel)ps).getColor());

									if (((ShapePixel)ps).getColor().getAlpha() < 255)
									{
										TransparencyAttributes ta2 = new TransparencyAttributes();
										ta2.setTransparencyMode(TransparencyAttributes.NICEST);
										ta2.setTransparency((255 - ((ShapePixel)ps).getColor().getAlpha()) / 255.0f);
										app2.setTransparencyAttributes(ta2);
									}

									app2.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

									xyz = new Point3d(x, y, 0);
									xayaza = new Point3d(0, 0, a);

									AddStaticObject(objScale, name, xyz, (float)a, objColor.get());
									
									break;
								}
							}
						}
					}
				}
			}
			
			
			i++;
		}
		ao.getEngine().run();*/
	}
	//
	private void AddStaticObject(TransformGroup objChild, String name, Point3d xyz, double a, Color color)
	{
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		app.setColoringAttributes(ca);
		objColor = new Color3f(color);

		if (color.getAlpha() < 255)
		{
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency((255 - color.getAlpha()) / 255.0f);
			app.setTransparencyAttributes(ta);
		}

		app.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

		
		
	}
	boolean hex;
	NgnTask ngnTask;
	public Grid setParameters( boolean bHex){
		//updateList.clear();
		ngnTask.done();
		List<BranchGroup> lstRemove = new LinkedList<BranchGroup>();
		for(Object a:updateGroup.keySet())
			lstRemove.add(updateGroup.get(a));

		hex=bHex;
		Grid grid = Context.getGrid();
		ngnTask = new NgnTask();
		ngnTask.doInBackground();
		updateGroup.clear();
		return  grid;

	}
	public void init(){
		//updateList.clear();
		updateGroup.clear();
		
    	ngnTask = new NgnTask();
    	setParameters(hex);
	};
	public Abstract3DPanel(int dim, boolean bHex)
	{
		hex=bHex;

				//ngnTask.doInBackground();

		
		//setLayout(new BorderLayout());
		
	/*	setupVariableObjectsTaskComplite = false;
		int cellSize=50;
		for(SimgraphNode a:grid.getObjects())
		{
			//SimgraphNode a=grid.getObjects().iterator().next();
			Sphere s = new  Sphere(30);
			GridPoint pnt=grid.getLocation(a);
			float x = CommonGraphics.getX(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
			float y = CommonGraphics.getY(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
			float z = CommonGraphics.getZ(pnt.getX()*cellSize, pnt.getY()*cellSize,pnt.getZ()*cellSize);
			
			BranchGroup objChild = new BranchGroup();
			objChild.setCapability(BranchGroup.ALLOW_DETACH);
			objChild.addChild(createPrimitive(s, a.getColor(), new Point3d(x - 100,y - 100,z - 2000), new Point3d(0,0,0)));
			objScaleVariable.addChild(objChild);
			updateGroup.put(a, objChild);
		}
		setupVariableObjectsTaskComplite = true;
*/
		
	}
	//
	private Canvas3D createUniverse()
	{
		// Get the preferred graphics configuration for the default screen
		java.awt.GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		// Create a Canvas3D using the preferred configuration
		Canvas3D canvas3d = new Canvas3D(config);

		// Create simple universe with view branch
		univ = new SimpleUniverse(canvas3d);
		bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 2200.0);

		PlatformGeometry pg = new PlatformGeometry();
		pg.setBounds(bounds);

		// Set up the ambient light
		Color3f ambientLightColor = new Color3f(0.2f, 0.2f, 0.2f);
		ambientLight = new AmbientLight(ambientLightColor);
		ambientLight.setInfluencingBounds(bounds);
		ambientLight.setCapability(DirectionalLight.ALLOW_SCOPE_READ);
		ambientLight.setCapability(DirectionalLight.ALLOW_SCOPE_WRITE);
		pg.addChild(ambientLight);

		// Set up the directional lights
		Color3f directionalLight1Color = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f directionalLight1Direction  = new Vector3f(1.0f, 1.0f, 1.0f);
		Color3f directionalLight2Color = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f directionalLight2Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);

		directionalLight1 = new DirectionalLight(directionalLight1Color, directionalLight1Direction);
		directionalLight1.setInfluencingBounds(bounds);
		directionalLight1.setCapability(DirectionalLight.ALLOW_SCOPE_READ);
		directionalLight1.setCapability(DirectionalLight.ALLOW_SCOPE_WRITE);
		pg.addChild(directionalLight1);

		directionalLight2 = new DirectionalLight(directionalLight2Color, directionalLight2Direction);
		directionalLight2.setInfluencingBounds(bounds);
		directionalLight2.setCapability(DirectionalLight.ALLOW_SCOPE_READ);
		directionalLight2.setCapability(DirectionalLight.ALLOW_SCOPE_WRITE);
		pg.addChild(directionalLight2);

		ViewingPlatform viewingPlatform = univ.getViewingPlatform();
		viewingPlatform.setPlatformGeometry(pg);
		viewingPlatform.setNominalViewingTransform();

		steerTG = viewingPlatform.getViewPlatformTransform();
		t3d = new Transform3D();
		steerTG.getTransform(t3d);

		sliderPoint = new Point3d();
		sliderPoint.set(0, 0, radius);

		sliderPoint_ = new Point3d();
		sliderPoint_.set(0, 0, radius);

		sliderPointCenter = new Point3d();
		sliderPointCenter.set(0, 0, 0);

		sliderPointCenter_ = new Point3d();
		sliderPointCenter_.set(0, 0, 0);

		t3d.lookAt(sliderPoint, sliderPointCenter, new Vector3d(0, 1, 0));
		t3d.invert();
		steerTG.setTransform(t3d);

		OrbitBehavior orbit = new OrbitBehavior(canvas3d, OrbitBehavior.REVERSE_ALL);
		orbit.setSchedulingBounds(bounds);
		viewingPlatform.setViewPlatformBehavior(orbit);

		//univ.getViewer().getView().setMinimumFrameCycleTime(FRAMERATE);

		univ.getViewer().getView().setFrontClipDistance(0.01);
		univ.getViewer().getView().setBackClipDistance(1000);
		return canvas3d;
	}
	//главная штука
	private BranchGroup createSceneGraph()
	{
		BranchGroup objRoot = new BranchGroup();
		objScaleVariable = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setScale(0.005);
		objScaleVariable.setTransform(t3d);
		objScaleVariable.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		objScaleVariable.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		objScaleVariable.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		objRoot.addChild(objScaleVariable);
		ambientLight.addScope(objScaleVariable);
		directionalLight1.addScope(objScaleVariable);
		directionalLight2.addScope(objScaleVariable);
		objRoot.compile();
		return objRoot;
	}
	//
	private BranchGroup createSceneGraphStatic()
	{
		BranchGroup objRoot = new BranchGroup();
		TransformGroup objScale = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setScale(0.005);
		objScale.setTransform(t3d);
		objRoot.addChild(objScale);

		SetupStaticObjects(objScale);

		ambientLight.addScope(objScale);
		directionalLight1.addScope(objScale);
		directionalLight2.addScope(objScale);
		objRoot.compile();
		return objRoot;
	}
	//
	private BranchGroup createSceneGraphAxis()
	{
		BranchGroup objRoot = new BranchGroup();
		TransformGroup objScale = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setScale(0.002);
		objScale.setTransform(t3d);
		objRoot.addChild(objScale);

		Appearance appZ = new Appearance();
		ColoringAttributes caZ = new ColoringAttributes();
		appZ.setColoringAttributes(caZ);
		Color3f objColorZ = new Color3f(Color.blue);
		appZ.setMaterial(new Material(objColorZ, black, objColorZ, white, 80.0f));

		Appearance appX = new Appearance();
		ColoringAttributes caX = new ColoringAttributes();
		appX.setColoringAttributes(caX);
		Color3f objColorX = new Color3f(Color.red);
		appX.setMaterial(new Material(objColorX, black, objColorX, white, 80.0f));

		Appearance appY = new Appearance();
		ColoringAttributes caY = new ColoringAttributes();
		appY.setColoringAttributes(caY);
		Color3f objColorY = new Color3f(Color.green);
		appY.setMaterial(new Material(objColorY, black, objColorY, white, 80.0f));

		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparencyMode(TransparencyAttributes.NICEST);
		ta.setTransparency(0.4f);
		appZ.setTransparencyAttributes(ta);
		appX.setTransparencyAttributes(ta);
		appY.setTransparencyAttributes(ta);

		//z
		Cylinder cylinderZ = new Cylinder(1, 20, appZ);
		Point3d xyz = new Point3d(0, 0, 10);
		Point3d xayaza = new Point3d(PI / 2., 0, 0);
		objScale.addChild(createPrimitive(cylinderZ, Color.black, xyz, xayaza));

		Cone coneZ = new Cone(2, 10, appZ);
		xyz = new Point3d(0, 0, 25);
		objScale.addChild(createPrimitive(coneZ, Color.black, xyz, xayaza));

		Font3D f3d = new Font3D(new Font("Arial", Font.BOLD, 5), new FontExtrusion());

		Point3f textPtZ = new Point3f(-1.8f, 4.0f, 0.0f);
		Text3D txtZ = new Text3D(f3d, "Z", textPtZ); 
		OrientedShape3D textShapeZ = new OrientedShape3D();
		textShapeZ.setGeometry(txtZ);
		textShapeZ.setAppearance(appZ);
		textShapeZ.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
		Point3f rotationPtZ = new Point3f(0.0f, 0.0f, 32.0f);
		textShapeZ.setRotationPoint(rotationPtZ);
		objScale.addChild(textShapeZ);

		//x
		Cylinder cylinderX = new Cylinder(1, 20, appX);
		xyz = new Point3d(10, 0, 0);
		xayaza = new Point3d(0, 0, PI / 2.);
		objScale.addChild(createPrimitive(cylinderX, Color.black, xyz, xayaza));

		Cone coneX = new Cone(2, 10, appX);
		xyz = new Point3d(25, 0, 0);
		objScale.addChild(createPrimitive(coneX, Color.black, xyz, xayaza));

		Point3f textPtX = new Point3f(30.2f, 0.0f, 0.0f);
		Text3D txtX = new Text3D(f3d, "X", textPtX); 
		OrientedShape3D textShapeX = new OrientedShape3D();
		textShapeX.setGeometry(txtX);
		textShapeX.setAppearance(appX);
		textShapeX.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
		Point3f rotationPtX = new Point3f(32.0f, 0.0f, 0.0f);
		textShapeX.setRotationPoint(rotationPtX);
		objScale.addChild(textShapeX);

		//y
		Cylinder cylinderY = new Cylinder(1, 20, appY);
		xyz = new Point3d(0, -10, 0);
		xayaza = new Point3d(0, 0, -PI);
		objScale.addChild(createPrimitive(cylinderY, Color.black, xyz, xayaza));

		Cone coneY = new Cone(2, 10, appY);
		xyz = new Point3d(0, -25, 0);
		objScale.addChild(createPrimitive(coneY, Color.black, xyz, xayaza));

		Point3f textPtY = new Point3f(-1.8f, -32.0f, 0.0f);
		Text3D txtY = new Text3D(f3d, "Y", textPtY); 
		OrientedShape3D textShapeY = new OrientedShape3D();
		textShapeY.setGeometry(txtY);
		textShapeY.setAppearance(appY);
		textShapeY.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
		Point3f rotationPtY = new Point3f(0.0f, -32.0f, 0.0f);
		textShapeY.setRotationPoint(rotationPtY);
		objScale.addChild(textShapeY);

		ambientLight.addScope(objScale);
		directionalLight1.addScope(objScale);
		directionalLight2.addScope(objScale);
		objRoot.compile();
		return objRoot;
	}
	//
	private BranchGroup createSceneGraphSky()
	{
		/*BranchGroup objRoot = new BranchGroup();
		objScaleSky = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setScale(0.005);
		objScaleSky.setTransform(t3d);
		objRoot.addChild(objScaleSky);

		Background bgNode = new Background(new Color3f(0.17f, 0.65f, 0.92f));
		bgNode.setApplicationBounds(bounds);
		objRoot.addChild(bgNode);

		Appearance backgroundApp = new Appearance();
		ColoringAttributes backgroundCa = new ColoringAttributes();
		backgroundApp.setColoringAttributes(backgroundCa);
		objColor = new Color3f(new Color3f(0.17f, 0.65f, 0.92f));
		backgroundApp.setMaterial(new Material(objColor, black, objColor, white, 80.0f));

		PolygonAttributes polyAttrib = new PolygonAttributes();
	    polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
	    polyAttrib.setBackFaceNormalFlip(true);
		backgroundApp.setPolygonAttributes(polyAttrib);

		Sphere skydome = new Sphere(2200.0f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_NORMALS_INWARD | Sphere.GENERATE_TEXTURE_COORDS, 50, backgroundApp);
		//objScale.addChild(createObject(skydome, xyz, -PI / 2., 0, 0));
		TransformGroup objSpin = new TransformGroup(t3d);
		objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objSpin.addChild(createPrimitive(skydome, null, new Point3d(0, 20, 0), new Point3d(0, 0, 0)));
		Alpha rotationAlpha = new Alpha(-1, 2000000);
		Transform3D yAxis = new Transform3D();
		rotator = new RotationInterpolator(rotationAlpha, objSpin, yAxis, 0.0f, (float)(2 * PI));
		rotationAlpha = new Alpha(-1, 2000000 / (jComboBoxTimeScale.getSelectedIndex() + 1));
		rotator.setAlpha(rotationAlpha);

		BoundingSphere bs = new BoundingSphere();

		rotator.setSchedulingBounds(bs);
		objSpin.addChild(rotator);
		Transform3D t3d_ = new Transform3D();
		t3d_.rotX(PI / 2.);
		TransformGroup tg = new TransformGroup(t3d_);
		tg.addChild(objSpin);
		objScaleSky.addChild(tg);

		// Set up the ambient light
		Color3f ambientSkyLightColor = new Color3f(0.8f, 0.8f, 0.8f);
		ambientSkyLight = new AmbientLight(ambientSkyLightColor);
		ambientSkyLight.setInfluencingBounds(bs);
		ambientSkyLight.setCapability(AmbientLight.ALLOW_COLOR_READ);
		ambientSkyLight.setCapability(AmbientLight.ALLOW_COLOR_WRITE);
		objScaleSky.addChild(ambientSkyLight);
		ambientLight.addScope(objScaleSky);

		ambientFog = new ExponentialFog();
		ambientFog.setColor(ambientSkyLightColor);
		ambientFog.setDensity(0);
		ambientFog.setCapability(Fog.ALLOW_COLOR_WRITE);
		ambientFog.setCapability(ExponentialFog.ALLOW_DENSITY_WRITE);
		ambientFog.setInfluencingBounds(bounds);

		objScaleSky.addChild(ambientFog);
		objScaleSky.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		objScaleSky.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		objScaleSky.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

		objRoot.compile();
		return objRoot;*/
		return null;
	}
	//
	private void updateTimerActionPerformed(java.awt.event.ActionEvent evt)
	{
		if (setupVariableObjectsTaskComplite)
		{
			SetupVariableObjectsTask setupVariableObjectsTask = new SetupVariableObjectsTask(0);
			setupVariableObjectsTask.doInBackground();
		}
		else
		{
			System.out.println("Time Exception");
		}
	}	

	
	/*
	USER INTERFACE
	 */

	private JPanel drawingPanel;
	private JButton jButtonStart;
	private JComboBox jComboBoxTimeScale, jComboBoxFPS, jComboBoxNamedViews;
	private JToolBar toolBar;
	private JSlider jSliderZoom, jSliderBias, jSliderTurn;
	private JScrollBar jScrollBarH, jScrollBarV;
	private javax.swing.Timer updateTimer;

	private void ngn()
	{

		setLayout(new java.awt.BorderLayout());
		setPreferredSize(new java.awt.Dimension(100, 100));
		drawingPanel = new JPanel();
		drawingPanel.setLayout(new java.awt.BorderLayout());
		drawingPanel.setPreferredSize(new java.awt.Dimension(920, 620));
		add(drawingPanel, java.awt.BorderLayout.CENTER);

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		//toolBar.setOrientation(JToolBar.VERTICAL);
		toolBar.setVisible(true);
		add("North", toolBar);

		jButtonStart = new JButton();
		jButtonStart.setFocusable(false);
		jButtonStart.setPreferredSize(new java.awt.Dimension(42, 42));
		jButtonStart.setEnabled(true);
		jButtonStart.setIcon(new ImageIcon("IconStart.png"));
		jButtonStart.setToolTipText("Start");
		jButtonStart.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{jButtonStartActionPerformed(evt);}
		});
		//toolBar.add(jButtonStart);
		//toolBar.addSeparator();
		JLabel jLabelTimeScale = new JLabel();
		jLabelTimeScale.setFont(new java.awt.Font(jLabelTimeScale.getFont().getFontName(), java.awt.Font.PLAIN, 10));
		jLabelTimeScale.setText("<html>Time<br>Scale:</html>");
		//toolBar.add(jLabelTimeScale);

		jComboBoxTimeScale = new JComboBox();
		jComboBoxTimeScale.setModel(new DefaultComboBoxModel(new String[] { " x1", " x2", " x4", " x8", " x16", " x32", " x64"}));
		//jComboBoxTimeScale.setSelectedIndex((int)(log(ao.getEngine().getRealTimeScale()) / log(2)));
		jComboBoxTimeScale.setLightWeightPopupEnabled(false);
		jComboBoxTimeScale.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				jComboBoxTimeScaleActionPerformed(evt);
			}
		});
		//toolBar.add(jComboBoxTimeScale);
		//toolBar.addSeparator();

		JLabel jLabelFPS = new JLabel();
		jLabelFPS.setFont(new java.awt.Font(jLabelFPS.getFont().getFontName(), java.awt.Font.PLAIN, 10));
		jLabelFPS.setText("FPS:");
		//toolBar.add(jLabelFPS);

		jComboBoxFPS = new JComboBox();
		jComboBoxFPS.setModel(new DefaultComboBoxModel(new String[] {" 1", " 5", " 10", " 15", " 20", " 25"}));
		jComboBoxFPS.setSelectedIndex(4);

		jComboBoxFPS.setLightWeightPopupEnabled(false);
		jComboBoxFPS.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				jComboBoxFPSActionPerformed(evt);
			}
		});
		//toolBar.add(jComboBoxFPS);
		//toolBar.addSeparator();

		JLabel jLabelNamedViews = new JLabel();
		jLabelNamedViews.setFont(new java.awt.Font(jLabelNamedViews.getFont().getFontName(), java.awt.Font.PLAIN, 10));
		jLabelNamedViews.setText("<html>Named<br>Views:</html>");
		//toolBar.add(jLabelNamedViews);

		jComboBoxNamedViews = new JComboBox();
		jComboBoxNamedViews.setModel(new DefaultComboBoxModel(new String[] { "Custom"}));
		jComboBoxNamedViews.setSelectedIndex(0);

	
		jComboBoxNamedViews.setLightWeightPopupEnabled(false);
		jComboBoxNamedViews.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				jComboBoxNamedViewsActionPerformed(evt);
			}
		});
		//toolBar.add(jComboBoxNamedViews);
		//toolBar.addSeparator();

		JLabel jLabel2 = new JLabel();
		jLabel2.setFont(new java.awt.Font(jLabel2.getFont().getFontName(), java.awt.Font.PLAIN, 10));
		jLabel2.setText("Zoom:");
		toolBar.add(jLabel2);

		jSliderZoom = new JSlider();
		jSliderZoom.setAutoscrolls(true);
		jSliderZoom.setPreferredSize(new java.awt.Dimension(276, 42));
		jSliderZoom.setFont(new java.awt.Font(jSliderZoom.getFont().getFontName(), java.awt.Font.PLAIN, 9));
		jSliderZoom.setValue(100);
		jSliderZoom.setMinimum(50);
		jSliderZoom.setMaximum(700);
		jSliderZoom.setPaintTicks(true);
		jSliderZoom.setPaintLabels(true);
		jSliderZoom.setMajorTickSpacing(50);
		jSliderZoom.setMinorTickSpacing(10);
		jSliderZoom.setSnapToTicks(true);
		jSliderZoom.addChangeListener(new javax.swing.event.ChangeListener()
		{
			public void stateChanged(javax.swing.event.ChangeEvent evt)
			{
				jSliderZoomStateChanged(evt);
			}
		});

		toolBar.add(jSliderZoom);
		toolBar.addSeparator();

		JLabel jLabelBias = new JLabel();
		jLabelBias.setFont(new java.awt.Font(jLabelBias.getFont().getFontName(), java.awt.Font.PLAIN, 10));
		jLabelBias.setText("Bias:");
		toolBar.add(jLabelBias);

		jSliderBias = new JSlider();
		jSliderBias.setAutoscrolls(true);
		jSliderBias.setPreferredSize(new java.awt.Dimension(126, 42));
		jSliderBias.setFont(new java.awt.Font(jSliderBias.getFont().getFontName(), java.awt.Font.PLAIN, 9));
		jSliderBias.setValue(0);
		jSliderBias.setMinimum(1);
		jSliderBias.setMaximum(89);
		jSliderBias.setPaintTicks(true);
		jSliderBias.setPaintLabels(true);
		jSliderBias.setMajorTickSpacing(44);
		jSliderBias.setMinorTickSpacing(4);
		jSliderBias.setSnapToTicks(true);
		jSliderBias.addChangeListener(new javax.swing.event.ChangeListener()
		{
			public void stateChanged(javax.swing.event.ChangeEvent evt)
			{
				jSliderBiasStateChanged(evt);
			}
		});

		toolBar.add(jSliderBias);
		toolBar.addSeparator();

		JLabel jLabelTurn = new JLabel();
		jLabelTurn.setFont(new java.awt.Font(jLabelTurn.getFont().getFontName(), java.awt.Font.PLAIN, 10));
		jLabelTurn.setText("Turn:");
		toolBar.add(jLabelTurn);

		jSliderTurn = new JSlider();
		jSliderTurn.setAutoscrolls(true);
		jSliderTurn.setPreferredSize(new java.awt.Dimension(138, 42));
		jSliderTurn.setFont(new java.awt.Font(jSliderTurn.getFont().getFontName(), java.awt.Font.PLAIN, 9));
		jSliderTurn.setValue(0);
		jSliderTurn.setMinimum(-180);
		jSliderTurn.setMaximum(180);
		jSliderTurn.setPaintTicks(true);
		jSliderTurn.setPaintLabels(true);
		jSliderTurn.setMajorTickSpacing(90);
		jSliderTurn.setMinorTickSpacing(15);
		jSliderTurn.setSnapToTicks(true);
		jSliderTurn.addChangeListener(new javax.swing.event.ChangeListener()
		{
			public void stateChanged(javax.swing.event.ChangeEvent evt)
			{
				jSliderTurnStateChanged(evt);
			}
		});

		toolBar.add(jSliderTurn);

		Canvas3D c = createUniverse();
		drawingPanel.add(c, java.awt.BorderLayout.CENTER);

		jScrollBarH = new JScrollBar();
		jScrollBarH.setOrientation(JScrollBar.HORIZONTAL);
		jScrollBarH.setValue(0);
		jScrollBarH.setMinimum(-1000);
		jScrollBarH.setMaximum(1000);
		jScrollBarH.addAdjustmentListener(new java.awt.event.AdjustmentListener()
		{
			public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
			{
				jScrollBarHAdjustmentValueChanged(evt);
			}
		});
		add("South", jScrollBarH);

		jScrollBarV = new JScrollBar();
		jScrollBarV.setOrientation(JScrollBar.VERTICAL);
		jScrollBarV.setValue(0);
		jScrollBarV.setMinimum(-1000);
		jScrollBarV.setMaximum(1000);
		jScrollBarV.addAdjustmentListener(new java.awt.event.AdjustmentListener()
		{
			public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
			{
				jScrollBarVAdjustmentValueChanged(evt);
			}
		});
		add("East", jScrollBarV);

		BranchGroup sceneStaticObjects = createSceneGraphStatic();
		univ.addBranchGraph(sceneStaticObjects);

		sceneVariableObjects = createSceneGraph();
		univ.addBranchGraph(sceneVariableObjects);

		BranchGroup sceneAxis = createSceneGraphAxis();
		univ.addBranchGraph(sceneAxis);

		jSliderBias.setValue(45);
		jSliderZoom.setValue(100);
		jSliderTurn.setValue(-45);

		jScrollBarH.setValue(0);
		jScrollBarV.setValue(0);

		//pack();

		setVisible(true);
		
		//javax.swing.JOptionPane.showMessageDialog(this, "<html>THIS IS A DEMO VERSION OF <font color=navy><b>thirD</b></font> LIBRARY<br>PLEASE CONTACT US: <font color=navy><u>imlab.asoiu@gmail.com</u></font></html>", "DEMO VERSION", javax.swing.JOptionPane.INFORMATION_MESSAGE);

		updateTimer = new javax.swing.Timer(FRAMERATE, new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				updateTimerActionPerformed(evt);
			}
		});

		updateTimer.start();
	}

	private void formWindowClosed(java.awt.event.WindowEvent evt)
	{
		univ.cleanup();
	}

	private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt)
	{
	
	}

	private void jComboBoxTimeScaleActionPerformed(java.awt.event.ActionEvent evt)
	{
		//ao.getEngine().setRealTimeScale(pow(2, jComboBoxTimeScale.getSelectedIndex()));
		Alpha rotationAlpha = new Alpha(-1, 2000000 / (jComboBoxTimeScale.getSelectedIndex() + 1));
		rotator.setAlpha(rotationAlpha);

		jComboBoxNamedViews.setSelectedIndex(0);
	}

	private void jComboBoxFPSActionPerformed(java.awt.event.ActionEvent evt)
	{
		if (jComboBoxFPS.getSelectedIndex() == 0)
			FRAMERATE = 1000 / 1;
		if (jComboBoxFPS.getSelectedIndex() == 1)
			FRAMERATE = 1000 / 5;
		if (jComboBoxFPS.getSelectedIndex() == 2)
			FRAMERATE = 1000 / 10;
		if (jComboBoxFPS.getSelectedIndex() == 3)
			FRAMERATE = 1000 / 15;
		if (jComboBoxFPS.getSelectedIndex() == 4)
			FRAMERATE = 1000 / 20;
		if (jComboBoxFPS.getSelectedIndex() == 5)
			FRAMERATE = 1000 / 25;

		updateTimer.setDelay(FRAMERATE);
		univ.getViewer().getView().setMinimumFrameCycleTime(FRAMERATE);
	}

	private void jComboBoxNamedViewsActionPerformed(java.awt.event.ActionEvent evt)
	{
		if (jComboBoxNamedViews.getSelectedIndex() > 0)
		{
			int si = jComboBoxNamedViews.getSelectedIndex();
		
			jComboBoxNamedViews.setSelectedIndex(si);
		}
	}

	private void jSliderZoomStateChanged(javax.swing.event.ChangeEvent evt)
	{
		radius = 5 / (jSliderZoom.getValue() / 100.);

		sliderPoint.set(sliderPoint.getX(), sliderPoint.getY(), sliderPoint.getZ());
		sliderPoint_.set(sliderPoint_.getX(), sliderPoint_.getY(), sliderPoint_.getZ());

		sliderPointCenter.set(sliderPoint.getX(), sliderPoint.getY(), 0);

		double xx = radius * sin(toRadians(jSliderTurn.getValue())) * cos(toRadians(jSliderBias.getValue()));
		double yy = -radius * cos(toRadians(jSliderTurn.getValue())) * cos(toRadians(jSliderBias.getValue()));
		double zz = radius * sin(toRadians(jSliderBias.getValue()));

		sliderPoint.set(sliderPoint_.getX() + xx, sliderPoint_.getY() + yy, zz);
		sliderPointCenter.set(sliderPoint_.getX() + sliderPointCenter_.getX(), sliderPoint_.getY() + sliderPointCenter_.getY(), sliderPointCenter_.getZ());

		t3d.lookAt(sliderPoint, sliderPointCenter, new Vector3d(0, 0, 1));

		t3d.invert();
		steerTG.setTransform(t3d);

		jComboBoxNamedViews.setSelectedIndex(0);
	}

	private void jSliderBiasStateChanged(javax.swing.event.ChangeEvent evt)
	{
		steerTG.getTransform(t3d);

		double xx = radius * sin(toRadians(jSliderTurn.getValue())) * cos(toRadians(jSliderBias.getValue()));
		double yy = -radius * cos(toRadians(jSliderTurn.getValue())) * cos(toRadians(jSliderBias.getValue()));
		double zz = radius * sin(toRadians(jSliderBias.getValue()));

		sliderPoint.set(sliderPoint_.getX() + xx, sliderPoint_.getY() + yy, zz);
		sliderPointCenter.set(sliderPoint_.getX() + sliderPointCenter_.getX(), sliderPoint_.getY() + sliderPointCenter_.getY(), sliderPointCenter_.getZ());

		t3d.lookAt(sliderPoint, sliderPointCenter, new Vector3d(0, 0, 1));
		t3d.invert();
		steerTG.setTransform(t3d);

		jComboBoxNamedViews.setSelectedIndex(0);
	}

	private void jSliderTurnStateChanged(javax.swing.event.ChangeEvent evt)
	{
		steerTG.getTransform(t3d);

		double xx = radius * sin(toRadians(jSliderTurn.getValue())) * cos(toRadians(jSliderBias.getValue()));
		double yy = -radius * cos(toRadians(jSliderTurn.getValue())) * cos(toRadians(jSliderBias.getValue()));
		double zz = radius * sin(toRadians(jSliderBias.getValue()));

		sliderPoint.set(sliderPoint_.getX() + xx, sliderPoint_.getY() + yy, zz);
		sliderPointCenter.set(sliderPoint_.getX() + sliderPointCenter_.getX(), sliderPoint_.getY() + sliderPointCenter_.getY(), sliderPointCenter_.getZ());

		t3d.lookAt(sliderPoint, sliderPointCenter, new Vector3d(0, 0, 1));
		t3d.invert();
		steerTG.setTransform(t3d);

		jComboBoxNamedViews.setSelectedIndex(0);

		isTurn = true;
		double s2c2 = pow(sin(toRadians(jSliderTurn.getValue())), 2) + pow(cos(toRadians(jSliderTurn.getValue())), 2);
		jScrollBarH.setValue((int)(100 * (sliderPointCenter.getX() * cos(toRadians(jSliderTurn.getValue())) + sliderPointCenter.getY() * sin(toRadians(jSliderTurn.getValue()))) / s2c2));
		jScrollBarV.setValue((int)(100 * (sliderPointCenter.getX() * sin(toRadians(jSliderTurn.getValue())) - sliderPointCenter.getY() * cos(toRadians(jSliderTurn.getValue()))) / s2c2));
		isTurn = false;
	}

	private void jScrollBarHAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
	{
		if (!isTurn)
		{
			double ddx = cos(toRadians(jSliderTurn.getValue())) * jScrollBarH.getValue() / 100.0;
			double ddy = sin(toRadians(jSliderTurn.getValue())) * jScrollBarH.getValue() / 100.0;
			ddx += sin(toRadians(jSliderTurn.getValue())) * jScrollBarV.getValue() / 100.0;
			ddy -= cos(toRadians(jSliderTurn.getValue())) * jScrollBarV.getValue() / 100.0;

			sliderPoint.set(ddx, ddy, sliderPoint.getZ());
			sliderPoint_.set(ddx, ddy, sliderPoint_.getZ());

			jScrollBarHOld = jScrollBarH.getValue();
			jScrollBarVOld = jScrollBarV.getValue();

			sliderPointCenter.set(sliderPoint.getX(), sliderPoint.getY(), 0);

			double xx = radius * sin(toRadians(jSliderTurn.getValue())) * cos(toRadians(jSliderBias.getValue()));
			double yy = -radius * cos(toRadians(jSliderTurn.getValue())) * cos(toRadians(jSliderBias.getValue()));
			double zz = radius * sin(toRadians(jSliderBias.getValue()));

			sliderPoint.set(sliderPoint_.getX() + xx, sliderPoint_.getY() + yy, zz);
			sliderPointCenter.set(sliderPoint_.getX() + sliderPointCenter_.getX(), sliderPoint_.getY() + sliderPointCenter_.getY(), sliderPointCenter_.getZ());

			t3d.lookAt(sliderPoint, sliderPointCenter, new Vector3d(0, 0, 1));

			t3d.invert();
			steerTG.setTransform(t3d);

			jComboBoxNamedViews.setSelectedIndex(0);
		}
	}

	private void jScrollBarVAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt)
	{
		if (!isTurn)
		{
			double ddx = cos(toRadians(jSliderTurn.getValue())) * jScrollBarH.getValue() / 100.0;
			double ddy = sin(toRadians(jSliderTurn.getValue())) * jScrollBarH.getValue() / 100.0;
			ddx += sin(toRadians(jSliderTurn.getValue())) * jScrollBarV.getValue() / 100.0;
			ddy -= cos(toRadians(jSliderTurn.getValue())) * jScrollBarV.getValue() / 100.0;

			sliderPoint.set(ddx, ddy, sliderPoint.getZ());
			sliderPoint_.set(ddx, ddy, sliderPoint_.getZ());

			jScrollBarHOld = jScrollBarH.getValue();
			jScrollBarVOld = jScrollBarV.getValue();

			sliderPointCenter.set(sliderPoint.getX(), sliderPoint.getY(), 0);

			double xx = radius * sin(toRadians(jSliderTurn.getValue())) * cos(toRadians(jSliderBias.getValue()));
			double yy = -radius * cos(toRadians(jSliderTurn.getValue())) * cos(toRadians(jSliderBias.getValue()));
			double zz = radius * sin(toRadians(jSliderBias.getValue()));

			sliderPoint.set(sliderPoint_.getX() + xx, sliderPoint_.getY() + yy, zz);
			sliderPointCenter.set(sliderPoint_.getX() + sliderPointCenter_.getX(), sliderPoint_.getY() + sliderPointCenter_.getY(), sliderPointCenter_.getZ());

			t3d.lookAt(sliderPoint, sliderPointCenter, new Vector3d(0, 0, 1));

			t3d.invert();

			jComboBoxNamedViews.setSelectedIndex(0);
		}
	}
@Override
public void paint(Graphics g) {
	SetupVariableObjects();
	super.paint(g);

}
protected Simulation sim;
public void setSim(Simulation sim2) {
	 sim=sim2;

}
	
}