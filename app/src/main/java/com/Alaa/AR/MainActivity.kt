package com.Alaa.AR

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arFragment = fragment as ArFragment
        /**
         * Touch listener to detect when a user touches the ArScene plane to place a model
         */
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            setModelOnUi(hitResult)
        }
        @RequiresApi(Build.VERSION_CODES.N)
         fun setModelOnUi(hitResult: HitResult) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                loadModel(R.raw.model) { modelRenderable ->
                    //Used to get anchor point on scene where user tapped
                    val anchor = hitResult.createAnchor()
                    //Created an anchor node to attach the anchor with its parent
                    val anchorNode = AnchorNode(anchor)
                    //Added arSceneView as parent to the anchorNode. So our anchors will bind to arSceneView.
                    anchorNode.setParent(arFragment.arSceneView.scene)

                    //TransformableNode for out model. So that it can be rotated, scaled etc using gestures
                    val transformableNode = TransformableNode(arFragment.transformationSystem)
                    //Assigned anchorNode as parent so that our model stays at the position where user taps
                    transformableNode.setParent(anchorNode)
                    //Assigned the resulted model received from loadModel method to transformableNode
                    transformableNode.renderable = modelRenderable
                    //Sets this node as selected node by default
                    transformableNode.select()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setModelOnUi(hitResult: HitResult) {
        loadModel(R.raw.model) { modelRenderable ->
            //Used to get anchor point on scene where user tapped
            val anchor = hitResult.createAnchor()
            //Created an anchor node to attach the anchor with its parent
            val anchorNode = AnchorNode(anchor)
            //Added arSceneView as parent to the anchorNode. So our anchors will bind to arSceneView.
            anchorNode.setParent(arFragment.arSceneView.scene)

            //TransformableNode for out model. So that it can be rotated, scaled etc using gestures
            val transformableNode = TransformableNode(arFragment.transformationSystem)
            //Assigned anchorNode as parent so that our model stays at the position where user taps
            transformableNode.setParent(anchorNode)
            //Assigned the resulted model received from loadModel method to transformableNode
            transformableNode.renderable = modelRenderable
            //Sets this node as selected node by default
            transformableNode.select()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadModel(@RawRes model: Int, callback: (ModelRenderable) -> Unit) {
        ModelRenderable
            .builder()
            .setSource(this, model)
            .build()
            .thenAccept { modelRenderable ->
                callback(modelRenderable)
            }
    }
}
